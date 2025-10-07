package br.com.fiap.postech.service_notificacoes.repositories;

import br.com.fiap.postech.service_notificacoes.entities.Lembrete;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LembreteRepository extends JpaRepository<Lembrete, Long> {

    Optional<Lembrete> findByAgendamentoId(Long agendamentoId);

    List<Lembrete> findByStatusAndDataHoraEnvioBefore(String status, LocalDateTime data);
}