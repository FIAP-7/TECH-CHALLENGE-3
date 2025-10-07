package br.com.fiap.postech.service_historico.resolver;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import br.com.fiap.postech.service_historico.document.AgendamentoDocument;
import br.com.fiap.postech.service_historico.service.ConsultaService;

@Controller
public class HistoricoConsultaResolver {

    private ConsultaService service;

    public HistoricoConsultaResolver(ConsultaService service) {
        this.service = service;
    }

    @QueryMapping
     public List<AgendamentoDocument> agendamentos(@Argument Long pacienteId ,
     @Argument Boolean somentePassados) {

     boolean isFiltrarPorPaciente = pacienteId != null;
     boolean isFiltrarPassados = somentePassados != null && somentePassados;

     if (isFiltrarPorPaciente && isFiltrarPassados) {
     return service.findByPacienteIdAndDataHoraBefore(pacienteId ,
     OffsetDateTime.now());
	} else if (isFiltrarPorPaciente) {
		return service.findByPacienteId(pacienteId);
	} else if (isFiltrarPassados) {
		return service.findByDataHoraBefore(OffsetDateTime.now());
	}
        return service.findAll();
    }

    @QueryMapping
    public AgendamentoDocument agendamentoById(@Argument(name = "id") Long id) {
        return service.findById(id);
    }
}
