package com.athlos.smashback.service;

import com.athlos.smashback.model.Comprovante;
import com.google.gson.*;
import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.search.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

@Service
public class EmailReaderService {

    private static final String EMAIL = "lifehealthcomp@gmail.com";
    private static final String PASSWORD = "qsto adve zwlc wtzm";

    @Autowired
    private JavaMailSender mailSender;

    @Scheduled(fixedDelay = 60000)
    public void verificarEmails() {
        System.out.println("\uD83D\uDD04 Verificando emails às " + java.time.LocalDateTime.now());
        try {
            Store store = conectarEmail();
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);

            Message[] messages = inbox.search(new AndTerm(
                    new FlagTerm(new Flags(Flags.Flag.SEEN), false),
                    new OrTerm(
                            new SubjectTerm("Pagamento"),
                            new SubjectTerm("Boleto")
                    )
            ));

            for (Message message : messages) {
                Address[] from = message.getFrom();
                if (from == null || from.length == 0) continue;
                String remetente = from[0].toString();

                if (message.getContentType().contains("multipart")) {
                    Multipart multipart = (Multipart) message.getContent();

                    for (int i = 0; i < multipart.getCount(); i++) {
                        BodyPart part = multipart.getBodyPart(i);
                        if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                            File tempFile = salvarAnexoTemporariamente(part);

                            String jsonGemini = enviarImagemParaGemini(tempFile);
                            System.out.println("\uD83D\uDCE6 Imagem analisada pela Gemini\nResposta:");
                            System.out.println(jsonGemini);

                            Comprovante pagamento = extrairPagamentoDoGemini(jsonGemini);
                            if (pagamento != null && pagamento.getValor() != null) {
                                System.out.println("✅ Pagamento identificado: " + pagamento);
                                enviarEmailDeConfirmacao(remetente);
                            }

                            if (tempFile.exists()) tempFile.delete();
                        }
                    }
                }
            }

            inbox.close(false);
            store.close();
        } catch (Exception e) {
            System.err.println("❌ Erro ao verificar emails:");
            e.printStackTrace();
        }
    }

    private Store conectarEmail() throws Exception {
        Properties props = new Properties();
        props.put("mail.store.protocol", "imaps");
        Session session = Session.getDefaultInstance(props, null);
        Store store = session.getStore("imaps");
        store.connect("imap.gmail.com", EMAIL, PASSWORD);
        return store;
    }

    private File salvarAnexoTemporariamente(BodyPart part) throws Exception {
        String fileName = "temp_" + part.getFileName();
        File file = new File(fileName);
        try (InputStream is = part.getInputStream();
             FileOutputStream fos = new FileOutputStream(file)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }
        return file;
    }

    private String enviarImagemParaGemini(File file) {
        try {
            String apiKey = "AIzaSyCS_Nyk5_7eZE7dceMiZDngNJufOqWtKgI";
            String mimeType = "image/png";

            // Se PDF, converter para imagem primeiro
            if (file.getName().toLowerCase().endsWith(".pdf")) {
                file = converterPdfParaImagem(file);
            }

            byte[] imageBytes = java.nio.file.Files.readAllBytes(file.toPath());
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            JsonObject inlineData = new JsonObject();
            inlineData.addProperty("mimeType", mimeType);
            inlineData.addProperty("data", base64Image);

            JsonObject imagePart = new JsonObject();
            imagePart.add("inlineData", inlineData);

            JsonObject textPart = new JsonObject();
            textPart.addProperty(
                    "text",
                    """
                        Você receberá a imagem de um comprovante de pagamento.
                        Extraia e retorne somente um JSON puro, sem explicações, marcações ou formatação adicional.

                        O JSON deve conter os seguintes campos:
                        - nomeRemetente: nome de quem enviou o pagamento.
                        - valor: valor da transação, como número (ex: \\"120.00\\").
                        - dataEnvio: data e horário do pagamento.
                        - bancoOrigem: banco de onde saiu o dinheiro.
                        Se algum campo não estiver claramente presente, use null.

                        Lembre-se:
                        - Os campos podem estar com nomes diferentes (ex: \\"De\\", \\"Para\\", \\"Data da operação\\").
                        - Traga todas as datas no formato de LocalDateTime do java. 
                          Caso esteja diferente de uma data convencional (ex: \\"11/09/2001\\"), 
                          por exemplo no formato\\"11 de Setembro de 2001\\", faça a conversão
                        - Não inclua texto explicativo, apenas o JSON puro.
                    """);

            JsonArray parts = new JsonArray();
            parts.add(imagePart);
            parts.add(textPart);

            JsonObject content = new JsonObject();
            content.add("parts", parts);

            JsonArray contents = new JsonArray();
            contents.add(content);

            JsonObject requestBody = new JsonObject();
            requestBody.add("contents", contents);

            okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + apiKey)
                    .post(okhttp3.RequestBody.create(requestBody.toString(), okhttp3.MediaType.parse("application/json")))
                    .build();

            try (okhttp3.Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Erro Gemini: " + response.code() + " - " + response.body().string());
                }
                return response.body().string();
            }
        } catch (Exception e) {
            System.err.println("❌ Erro ao enviar imagem para Gemini:");
            e.printStackTrace();
            return null;
        }
    }

    private File converterPdfParaImagem(File pdfFile) throws IOException {
        PDDocument document = PDDocument.load(pdfFile);
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        BufferedImage image = pdfRenderer.renderImageWithDPI(0, 300, ImageType.RGB);
        File imageFile = new File("converted_temp.png");
        ImageIO.write(image, "png", imageFile);
        document.close();
        return imageFile;
    }

    private Comprovante extrairPagamentoDoGemini(String respostaGemini) {
        try {
            JsonObject respostaCompleta = JsonParser.parseString(respostaGemini).getAsJsonObject();

            String textoCrudo = respostaCompleta
                    .getAsJsonArray("candidates")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("content")
                    .getAsJsonArray("parts")
                    .get(0).getAsJsonObject()
                    .get("text").getAsString();

            String jsonLimpo = textoCrudo
                    .replaceAll("```json", "")
                    .replaceAll("```", "")
                    .trim();

            if (!jsonLimpo.startsWith("{") || !jsonLimpo.endsWith("}")) {
                throw new IllegalArgumentException("Resposta não contém JSON válido");
            }
            System.out.println("JSON processado: " + jsonLimpo);

            return new Gson().fromJson(jsonLimpo, Comprovante.class);

        } catch (Exception e) {
            System.err.println("❌ Erro crítico ao processar resposta do Gemini:");
            System.err.println("Resposta original: " + respostaGemini);
            e.printStackTrace();
            return null;
        }
    }

    private void enviarEmailDeConfirmacao(String nome) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "utf-8");

            helper.setTo(nome);
            helper.setSubject("Confirmação de Pagamento");
            helper.setText("Olá! O pagamento em nome de " + nome + " foi recebido com sucesso. Obrigado!", true);
            helper.setFrom(EMAIL);

            mailSender.send(message);
            System.out.println("\uD83D\uDCE8 Email de confirmação enviado para: " + nome);
        } catch (Exception e) {
            System.err.println("❌ Erro ao enviar email de confirmação:");
            e.printStackTrace();
        }
    }
}