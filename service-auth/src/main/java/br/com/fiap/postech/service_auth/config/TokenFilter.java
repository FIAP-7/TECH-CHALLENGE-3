package br.com.fiap.postech.service_auth.config;

import br.com.fiap.postech.service_auth.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class TokenFilter extends OncePerRequestFilter {
    private static final Logger logger = LogManager.getLogger(TokenFilter.class);
    private final TokenService tokenService;

    public TokenFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if ("/auth/login".equals(path)
                || "/auth/validate".equals(path)
                || path.startsWith("/swagger-ui")
                || "/swagger-ui.html".equals(path)
                || path.startsWith("/v3/api-docs"))
        {
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            TokenService.TokenValidationResult result = tokenService.validateToken(token);

            if (result.isValid()) {
                GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + result.getPerfil().toUpperCase());
                Authentication auth2 = new UsernamePasswordAuthenticationToken(result.getPerfil(), null, Collections.singletonList(authority));

                ((UsernamePasswordAuthenticationToken) auth2).setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(auth2);

                filterChain.doFilter(request, response);

                return;
            }else{
                logger.debug("Token nao autorizado" + path);
            }
        }else{
            logger.debug("Token nao localizado" + path);
        }

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}
