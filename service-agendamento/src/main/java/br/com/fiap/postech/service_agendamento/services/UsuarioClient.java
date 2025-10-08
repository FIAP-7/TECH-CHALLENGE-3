package br.com.fiap.postech.service_agendamento.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UsuarioClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${service-auth.base-url:http://service-auth:8083}")
    private String baseUrl;

    public UsuarioResponse buscarPorId(Long id){
        String url = String.format("%s/usuario/id/%d", baseUrl, id);
        ResponseEntity<UsuarioResponse> resp = restTemplate.getForEntity(url, UsuarioResponse.class);
        return resp.getBody();
    }

    public static record UsuarioResponse(Long id, String username, String name, String email, Boolean active) {}
}
