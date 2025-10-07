package br.com.fiap.postech.service_notificacoes.service;

import br.com.fiap.postech.service_notificacoes.dto.AgendamentoEventDTO;
import br.com.fiap.postech.service_notificacoes.entities.Lembrete;
import br.com.fiap.postech.service_notificacoes.repositories.LembreteRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LembreteAutomationService {

    private final LembreteRepository repository;

    public LembreteAutomationService(LembreteRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void handleAgendamentoEvent(AgendamentoEventDTO event) {
        Long agendamentoId = event.getAgendamentoId();
        Optional<Lembrete> existingLembrete = repository.findByAgendamentoId(agendamentoId);

        Lembrete lembrete = existingLembrete.orElseGet(Lembrete::new);

        if ("CANCELAMENTO".equals(event.getTipoEvento())) {
            if (existingLembrete.isPresent()) {
                lembrete.setStatus("CANCELADO");
                repository.save(lembrete);
            }
            return;
        }

        LocalDateTime dataEnvio = event.getDataHora().minusHours(24);

        lembrete.setAgendamentoId(agendamentoId);
        lembrete.setPacienteId(event.getPacienteId());
        lembrete.setDataHoraConsulta(event.getDataHora());
        lembrete.setDataHoraEnvio(dataEnvio);

        if (!"ENVIADO".equals(lembrete.getStatus())) {
            lembrete.setStatus("PENDENTE");
        }

        repository.save(lembrete);
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void verificarEEnviarLembretes() {

        List<Lembrete> lembretesParaEnviar = repository
                .findByStatusAndDataHoraEnvioBefore("PENDENTE", LocalDateTime.now());

        if (!lembretesParaEnviar.isEmpty()) {
            System.out.println("🤖 Encontrados " + lembretesParaEnviar.size() + " lembretes para envio.");
        }

        for (Lembrete lembrete : lembretesParaEnviar) {

            simularEnvio(lembrete);

            lembrete.setStatus("ENVIADO");
            repository.save(lembrete);
        }
    }

    private void simularEnvio(Lembrete lembrete) {
        System.out.println("📬 Enviando Lembrete de Consulta!");
        System.out.println("   Paciente ID: " + lembrete.getPacienteId());
        System.out.println("   Consulta agendada para: " + lembrete.getDataHoraConsulta());
    }
}