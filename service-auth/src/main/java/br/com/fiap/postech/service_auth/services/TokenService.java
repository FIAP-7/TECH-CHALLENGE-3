package br.com.fiap.postech.service_auth.services;

import br.com.fiap.postech.service_auth.entities.Usuario;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;

@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String secretKey;

    public String generateToken(Usuario usuario) {

        List<String> roles = new ArrayList<>();
        roles.add("ROLE_" + usuario.getRole().getName());

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);

        return Jwts.builder()
                .claims(claims)
                .subject(usuario.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15)) // 15 min
                .signWith(getSigningKey())
                .compact();
    }

    public Date extractExpiration(String token) {
        return getParser().parseSignedClaims(token).getPayload().getExpiration();
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    private JwtParser getParser() {
        return Jwts.parser()
                .verifyWith((SecretKey) getSigningKey())
                .build();
    }

    public String extractUsername(String token) {
        return getParser()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isTokenValid(String token, String username) {
        String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username)) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return getParser()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new java.util.Date());
    }
}
