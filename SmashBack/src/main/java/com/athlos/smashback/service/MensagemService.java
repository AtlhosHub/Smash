package com.athlos.smashback.service;

import com.athlos.smashback.model.Aluno;
import com.athlos.smashback.model.Mensalidade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class MensagemService {

    @Autowired
    private JavaMailSender mailSender;

    private static final String REMETENTE = "acdnbvilaformosa@gmail.com";


    public void enviarFalhaValorInsuficiente(Aluno aluno,
                                             double valorRecebido,
                                             double valorNecessario,
                                             String emailDestino) {
        try {
            String nomePara = obterNomeParaEmail(aluno);

            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, false, "UTF-8");

            helper.setFrom(REMETENTE);
            helper.setTo(emailDestino);
            helper.setSubject("Falha na Confirmação de Pagamento");

            StringBuilder sb = new StringBuilder();
            sb.append("<p>Olá, ").append(nomePara).append(".</p>");
            sb.append("<p>Recebemos seu comprovante no valor de <b>R$ ")
                    .append(String.format("%.2f", valorRecebido))
                    .append("</b>, mas o valor mínimo para quitar a próxima mensalidade é de <b>R$ ")
                    .append(String.format("%.2f", valorNecessario))
                    .append("</b>.</p>");
            sb.append("<p>Por favor, verifique o valor e nos envie um novo comprovante.</p>");

            helper.setText(sb.toString(), true);
            mailSender.send(msg);

        } catch (Exception e) {
            System.err.println("❌ Erro ao enviar e-mail de falha (valor insuficiente):");
            e.printStackTrace();
        }
    }


    public void enviarConfirmacaoPagamento(Aluno aluno,
                                           List<Mensalidade> mensalidadesPagas,
                                           double valorTotal,
                                           String emailDestino) {
        try {
            String nomePara = obterNomeParaEmail(aluno);

            String listaMeses = mensalidadesPagas.stream()
                    .map(m -> {
                        String nomeMes = m.getDataVencimento()
                                .getMonth()
                                .getDisplayName(TextStyle.FULL, new Locale("pt","BR"));
                        nomeMes = nomeMes.substring(0,1).toUpperCase() + nomeMes.substring(1);
                        int ano = m.getDataVencimento().getYear();
                        return nomeMes + " " + ano;
                    })
                    .collect(Collectors.joining(", "));

            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, false, "UTF-8");

            helper.setFrom(REMETENTE);
            helper.setTo(emailDestino);
            helper.setSubject("Confirmação de Pagamento");

            StringBuilder sb = new StringBuilder();
            sb.append("<p>Olá, ").append(nomePara).append("!</p>");
            sb.append("<p>Recebemos seu comprovante no valor de <b>R$ ")
                    .append(String.format("%.2f", valorTotal))
                    .append("</b> e processamos com sucesso as mensalidades de: <b>")
                    .append(listaMeses)
                    .append("</b>.</p>");
            sb.append("<p>Obrigado por manter suas mensalidades em dia! 😊</p>");

            helper.setText(sb.toString(), true);
            mailSender.send(msg);

        } catch (Exception e) {
            System.err.println("❌ Erro ao enviar e-mail de confirmação de pagamento:");
            e.printStackTrace();
        }
    }


    public void enviarErroGenerico(Aluno aluno,
                                   String motivo,
                                   String detalhes,
                                   String emailDestino) {
        try {
            String nomePara = obterNomeParaEmail(aluno);

            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, false, "UTF-8");

            helper.setFrom(REMETENTE);
            helper.setTo(emailDestino);
            helper.setSubject("Falha na Confirmação de Pagamento");

            StringBuilder sb = new StringBuilder();
            sb.append("<p>Olá, ").append(nomePara).append(".</p>");
            sb.append("<p>Ocorreu um problema ao processar seu comprovante:</p>");
            sb.append("<ul>");
            sb.append("<li><b>Motivo:</b> ").append(motivo).append("</li>");
            sb.append("<li><b>Detalhes:</b> ").append(detalhes).append("</li>");
            sb.append("</ul>");
            sb.append("<p>Por favor, corrija e tente novamente.</p>");

            helper.setText(sb.toString(), true);
            mailSender.send(msg);

        } catch (Exception e) {
            System.err.println("❌ Erro ao enviar e-mail de erro genérico:");
            e.printStackTrace();
        }
    }

    private String obterNomeParaEmail(Aluno aluno) {
        if (aluno.getNomeSocial() != null && !aluno.getNomeSocial().isBlank()) {
            return aluno.getNomeSocial();
        }
        return aluno.getNome();
    }
}