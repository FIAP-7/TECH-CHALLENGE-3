package br.com.fiap.postech.service_notificacoes.service;

import br.com.fiap.postech.service_notificacoes.dto.AgendamentoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationPublisherService {

    private static final Logger log = LoggerFactory.getLogger(NotificationPublisherService.class);

    private final JavaMailSender mailSender;

    @Value("${app.mail.from:no-reply@postech.com}")
    private String fromAddress;

    public NotificationPublisherService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendNotification(AgendamentoDTO agendamento) {
        try {
            if (agendamento == null || agendamento.paciente() == null) {
                log.warn("Agendamento ou paciente nulo recebido. Ignorando envio de email.");
                return;
            }

            String to = agendamento.paciente().email();
            if (to == null || to.isBlank()) {
                log.warn("Paciente sem email cadastrado. idAgendamento={} paciente={}", agendamento.id(), agendamento.paciente().nome());
                return;
            }

            String subject = "Lembrete de consulta";
            String medico = agendamento.medico() != null ? agendamento.medico().nome() : "médico(a)";
            String dataHora = agendamento.dataHora();
            String motivo = agendamento.motivo() != null ? agendamento.motivo() : "consulta";

            String body = "Olá, " + agendamento.paciente().nome() + ",\n\n" +
                    "Este é um lembrete da sua " + motivo +
                    " com " + medico +
                    " marcada para: " + dataHora + ".\n\n" +
                    "Caso não possa comparecer, por favor reagende com antecedência.\n\n" +
                    "Atenciosamente,\n" +
                    "Equipe Postech";

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromAddress);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
            log.info("Email de lembrete enviado com sucesso para {} referente ao agendamento {}", to, agendamento.id());
        } catch (Exception e) {
            log.error("Falha ao enviar email de lembrete para agendamento {}: {}", agendamento != null ? agendamento.id() : null, e.getMessage(), e);
        }
    }
}
