package br.com.fiap.postech.service_agendamento.controllers;

import br.com.fiap.postech.service_agendamento.entities.Agendamento;
import br.com.fiap.postech.service_agendamento.exceptions.RecursoNaoEncontradoException;
import br.com.fiap.postech.service_agendamento.services.AgendamentoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AgendamentoController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
public class AgendamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AgendamentoService service;

    private Agendamento buildAgendamentoEntity(Long id) {
        Agendamento agendamentoEntity = new Agendamento();
        agendamentoEntity.setId(id);
        agendamentoEntity.setPacienteId(1L);
        agendamentoEntity.setMedicoId(2L);
        agendamentoEntity.setDataHora(OffsetDateTime.parse("2025-01-01T10:00:00Z"));
        agendamentoEntity.setMotivo("Consulta");
        agendamentoEntity.setStatus(Agendamento.Status.AGENDADO);
        return agendamentoEntity;
    }

    @Test
    void postCriar_returns201AndBody() throws Exception {
        Agendamento salvo = buildAgendamentoEntity(100L);
        given(service.criar(any())).willReturn(salvo);

        String json = "{" +
                "\"pacienteId\":1," +
                "\"medicoId\":2," +
                "\"dataHora\":\"2025-01-01T10:00:00Z\"," +
                "\"motivo\":\"Consulta\"}";

        mockMvc.perform(post("/agendamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/agendamentos/100"))
                .andExpect(jsonPath("$.id", is(100)))
                .andExpect(jsonPath("$.pacienteId", is(1)))
                .andExpect(jsonPath("$.medicoId", is(2)))
                .andExpect(jsonPath("$.motivo", is("Consulta")));
    }

    @Test
    void putAtualizar_returns200() throws Exception {
        Agendamento atualizado = buildAgendamentoEntity(10L);
        atualizado.setMotivo("Remarcado");
        atualizado.setStatus(Agendamento.Status.REMARCADO);
        given(service.atualizar(eq(10L), any())).willReturn(atualizado);

        String json = "{" +
                "\"dataHora\":\"2025-01-02T10:30:00Z\"," +
                "\"status\":\"REMARCADO\"," +
                "\"motivo\":\"Remarcado\"}";

        mockMvc.perform(put("/agendamentos/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(10)))
                .andExpect(jsonPath("$.status", is("REMARCADO")))
                .andExpect(jsonPath("$.motivo", is("Remarcado")));
    }

    @Test
    void getBuscarPorId_returns200() throws Exception {
        given(service.buscarPorId(5L)).willReturn(buildAgendamentoEntity(5L));
        mockMvc.perform(get("/agendamentos/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.pacienteId", is(1)));
    }

    @Test
    void getBuscarPorId_notFound_returns404() throws Exception {
        given(service.buscarPorId(999L)).willThrow(new RecursoNaoEncontradoException("Agendamento não encontrado: 999"));
        mockMvc.perform(get("/agendamentos/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", containsString("999")));
    }

    @Test
    void getListarPorPaciente_returns200List() throws Exception {
        given(service.listarPorPaciente(1L)).willReturn(List.of(buildAgendamentoEntity(1L), buildAgendamentoEntity(2L)));
        mockMvc.perform(get("/agendamentos/paciente/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));
    }

    @Test
    void deleteRemover_returns204() throws Exception {
        doNothing().when(service).remover(8L);
        mockMvc.perform(delete("/agendamentos/8"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteRemover_notFound_returns404() throws Exception {
        doThrow(new RecursoNaoEncontradoException("Agendamento não encontrado: 7")).when(service).remover(7L);
        mockMvc.perform(delete("/agendamentos/7"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", containsString("7")));
    }
}
