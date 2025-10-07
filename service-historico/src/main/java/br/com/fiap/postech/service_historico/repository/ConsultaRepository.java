package br.com.fiap.postech.service_historico.repository;

import br.com.fiap.postech.service_historico.document.AgendamentoDocument;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultaRepository extends MongoRepository<AgendamentoDocument, Long> {

    List<AgendamentoDocument> findByPacienteId(Long pacienteId);

    List<AgendamentoDocument> findByDataHoraBefore(OffsetDateTime dataAtual);

    List<AgendamentoDocument> findByPacienteIdAndDataHoraBefore(Long pacienteId, OffsetDateTime dataAtual);
}
