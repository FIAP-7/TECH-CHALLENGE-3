package br.com.fiap.postech.service_notificacoes.service;

import org.springframework.stereotype.Service;

import br.com.fiap.postech.service_notificacoes.dto.AgendamentoDTO;

@Service
public class NotificationPublisherService {

    public void sendNotification(AgendamentoDTO agendamento) {
        String mensagem = "Agendamento: id - " + agendamento.id() + ", paciente - " + agendamento.paciente().nome() + ", data - " + agendamento.dataHora();
        System.out.println("Enviando notificação: " + mensagem);
    }
}
