package com.athlos.smashback.service;

import com.athlos.smashback.adapters.JavaTimeAdapters;
import com.athlos.smashback.model.Aluno;
import com.athlos.smashback.model.Comprovante;
import com.athlos.smashback.model.Mensalidade;
import com.athlos.smashback.repository.AlunoRepository;
import com.athlos.smashback.repository.ComprovanteRepository;
import com.athlos.smashback.repository.MensalidadeRepository;
import com.google.gson.*;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.search.*;
import jakarta.transaction.Transactional;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.*;
import com.athlos.smashback.model.enums.Status;

@Service
public class EmailReaderService {

    private static final String EMAIL = "lifehealthcomp@gmail.com";
    private static final String PASSWORD = "qsto adve zwlc wtzm";

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private MensalidadeRepository mensalidadeRepository;

    @Autowired
    private ComprovanteRepository comprovanteRepository;

    @Scheduled(fixedDelay = 60000)
    @Transactional
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

                String remetenteEmail = InternetAddress.toString(from)
                        .replaceAll(".*<([^>]+)>.*", "$1")
                        .trim();

                Optional<Aluno> alunoOpt = alunoRepository.findByEmailOrResponsavelEmail(remetenteEmail);

                if (alunoOpt.isEmpty()) {
                    System.out.println("⚠️ Email não vinculado a nenhum aluno: " + remetenteEmail);
                    continue;
                }

                Aluno aluno = alunoOpt.get();

                String nomeAluno = (aluno.getNomeSocial() != null && !aluno.getNomeSocial().isBlank())
                        ? aluno.getNomeSocial()
                        : aluno.getNome();

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
                                processarPagamento(aluno, pagamento, remetenteEmail);                            }

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
        File converted = null;
        try {
            String apiKey = "AIzaSyCS_Nyk5_7eZE7dceMiZDngNJufOqWtKgI";
            String mimeType = "image/png";

            if (file.getName().toLowerCase().endsWith(".pdf")) {
                converted = converterPdfParaImagem(file);
                file = converted;
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
                                - valor: valor da transação, como número (ex: "120.00").
                                - dataEnvio: data e horário do pagamento **no formato brasileiro (dd/MM/yyyy)**. 
                                  Converta para o formato ISO_LOCAL_DATE_TIME (yyyy-MM-dd'T'HH:mm:ss).
                                - bancoOrigem: banco de onde saiu o dinheiro.
                                Se algum campo não estiver claramente presente, use null.
                            
                                Exemplo de Conversão de Data:
                                - Data no comprovante: "01/04/2025 - 07:13:18" → "2025-04-01T07:13:18"
                            
                                Observações:
                                - Campos podem ter nomes variantes (ex: "Data da operação", "Data/Hora").
                                - Não inclua texto explicativo, apenas o JSON puro.
                            """
            );


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
        } finally {
            if (converted != null && converted.exists()) {
                if (!converted.delete()) {
                    System.err.println("⚠️ Não foi possível apagar o arquivo temporário convertido: " + converted.getName());
                }
            }
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

            Gson gson = JavaTimeAdapters
                    .registerAll(new GsonBuilder())
                    .create();

            return gson.fromJson(jsonLimpo, Comprovante.class);


        } catch (Exception e) {
            System.err.println("❌ Erro crítico ao processar resposta do Gemini:");
            System.err.println("Resposta original: " + respostaGemini);
            e.printStackTrace();
            return null;
        }
    }

    private void processarPagamento(Aluno aluno,
                                    Comprovante comprovante,
                                    String emailDestino) {
        try {
            comprovante.setAluno(aluno);
            Comprovante comprovanteSalvo = comprovanteRepository.save(comprovante);

            List<Mensalidade> mensalidades = mensalidadeRepository
                    .findByAlunoAndStatusInOrderByDataVencimentoAsc(
                            aluno,
                            List.of(Status.PENDENTE, Status.ATRASADO)
                    );

            double valorDisponivel = comprovanteSalvo.getValor();
            LocalDate dataPagamento = comprovanteSalvo.getDataEnvio().toLocalDate();

            List<Mensalidade> comDesconto = new ArrayList<>();
            for (Mensalidade m : mensalidades) {
                long diasAntecedencia = ChronoUnit.DAYS.between(dataPagamento, m.getDataVencimento());
                if (diasAntecedencia >= 12 && dataPagamento.isBefore(m.getDataVencimento())) {
                    comDesconto.add(m);
                }
            }

            if (!mensalidades.isEmpty()) {
                Mensalidade primeira = mensalidades.get(0);
                boolean aplicaDescPrimeira = comDesconto.contains(primeira);
                double valorPrimeira = aplicaDescPrimeira
                        ? primeira.getValor() - 10.0
                        : primeira.getValor();

                if (valorDisponivel < valorPrimeira) {
                    enviarEmailFalha(aluno, valorDisponivel, valorPrimeira, emailDestino);
                    System.out.println("❌ Valor insuficiente: precisava de R$ "
                            + valorPrimeira + ", recebeu R$ " + valorDisponivel);
                    return;
                }
            }

            for (Mensalidade m : mensalidades) {
                if (valorDisponivel <= 0) break;

                boolean aplicaDesc = comDesconto.contains(m);
                double valorFinal = aplicaDesc
                        ? m.getValor() - 10.0
                        : m.getValor();

                if (valorDisponivel >= valorFinal) {
                    if (aplicaDesc) {
                        System.out.println("🎉 Desconto aplicado em "
                                + m.getDataVencimento().getMonthValue() + "/"
                                + m.getDataVencimento().getYear());
                    }

                    m.setStatus(Status.PAGO);
                    m.setDataPagamento(comprovanteSalvo.getDataEnvio());
                    m.setComprovante(comprovanteSalvo);
                    m.setValor(valorFinal);
                    m.setFormaPagamento("Pix");

                    valorDisponivel -= valorFinal;
                    mensalidadeRepository.save(m);

                    System.out.println("✅ Mensalidade "
                            + m.getDataVencimento().getMonthValue() + "/"
                            + m.getDataVencimento().getYear() + " paga: R$ " + valorFinal);
                }
            }

            if (valorDisponivel > 0) {
                System.out.println("⚠️ Valor excedente: R$ " + valorDisponivel);
            }

            enviarEmailDeConfirmacao(aluno, comprovanteSalvo.getValor(), emailDestino);

        } catch (Exception e) {
            System.err.println("❌ Erro ao processar pagamento:");
            e.printStackTrace();
        }
    }



    private void enviarEmailDeConfirmacao(Aluno aluno,
                                          double recebido,
                                          String emailDestino) {
        try {
            String nome = (aluno.getNomeSocial() != null && !aluno.getNomeSocial().isBlank())
                    ? aluno.getNomeSocial()
                    : aluno.getNome();

            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, false, "UTF-8");

            helper.setTo(emailDestino);
            helper.setSubject("Confirmação de Pagamento");
            helper.setFrom(EMAIL);

            StringBuilder sb = new StringBuilder();
            sb.append("<p>Olá, ").append(nome).append("!</p>");
            sb.append("<p>Recebemos seu comprovante no valor de <b>R$ ")
                    .append(String.format("%.2f", recebido))
                    .append("</b> e processamos o pagamento com sucesso.</p>");
            sb.append("<p>Obrigado por manter suas mensalidades em dia! 😊</p>");

            helper.setText(sb.toString(), true);
            mailSender.send(msg);

            System.out.println("✉️ Email de confirmação enviado para: " + emailDestino);
        } catch (Exception e) {
            System.err.println("❌ Erro ao enviar email de confirmação:");
            e.printStackTrace();
        }
    }

    private void enviarEmailFalha(Aluno aluno,
                                  double recebido,
                                  double minimoNecessario,
                                  String emailDestino) {
        try {
            String nome = (aluno.getNomeSocial() != null && !aluno.getNomeSocial().isBlank())
                    ? aluno.getNomeSocial()
                    : aluno.getNome();

            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, false, "UTF-8");

            helper.setTo(emailDestino);
            helper.setSubject("Falha na Confirmação de Pagamento");
            helper.setFrom(EMAIL);

            StringBuilder sb = new StringBuilder();
            sb.append("<p>Olá, ").append(nome).append(".</p>");
            sb.append("<p>Recebemos seu comprovante no valor de <b>R$ ")
                    .append(String.format("%.2f", recebido))
                    .append("</b>, mas o valor mínimo necessário para quitação é de <b>R$ ")
                    .append(String.format("%.2f", minimoNecessario))
                    .append("</b>.</p>");
            sb.append("<p>Por favor, verifique o valor e envie um novo comprovante.</p>");

            helper.setText(sb.toString(), true);
            mailSender.send(msg);

            System.out.println("✉️ Email de falha enviado para: " + emailDestino);
        } catch (Exception e) {
            System.err.println("❌ Erro ao enviar email de falha:");
            e.printStackTrace();
        }
    }

}