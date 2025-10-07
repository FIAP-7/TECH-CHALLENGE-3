package br.com.fiap.postech.service_agendamento.controllers;

import br.com.fiap.postech.service_agendamento.exceptions.RecursoNaoEncontradoException;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GlobalExceptionHandlerTest {

    @Test
    void handleNotFound_returns404AndBody() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/agendamentos/999");

        ResponseEntity<Map<String, Object>> response = handler.handleNotFound(
                new RecursoNaoEncontradoException("Agendamento não encontrado: 999"), request);

        assertEquals(404, response.getStatusCode().value());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(404, body.get("status"));
        assertEquals("Not Found", body.get("error"));
        assertEquals("/agendamentos/999", body.get("path"));
        assertTrue(((String) body.get("message")).contains("999"));
        assertNotNull(body.get("timestamp"));
    }
}
