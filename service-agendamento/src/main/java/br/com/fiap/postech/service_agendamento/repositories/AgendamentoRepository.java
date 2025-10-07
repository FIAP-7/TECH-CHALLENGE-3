package br.com.fiap.postech.service_agendamento.repositories;

import br.com.fiap.postech.service_agendamento.entities.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    List<Agendamento> findByPacienteId(Long pacienteId);
}
