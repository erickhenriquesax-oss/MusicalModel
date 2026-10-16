package com.fag.musicalmodel.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fag.musicalmodel.model.UsuarioModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
public class TokenConfig {

    @Value("${api.security.token.secret}")
    private String token;

    public String generateToken(UsuarioModel usuario) {

        Algorithm algorithm = Algorithm.HMAC256(token);
        String role = (usuario.getUserRole() != null) ? usuario.getUserRole().getAuthority() : null;

        var jwtBuilder = JWT.create()
                .withClaim("idUsuario", usuario.getIdUsuario())
                .withSubject(usuario.getEmail())
                .withExpiresAt(Instant.now().plusSeconds(88888))
                .withIssuedAt(Instant.now());

        if (role != null) {
            jwtBuilder.withClaim("role", role);
        }

        return jwtBuilder.sign(algorithm);
    }

    public Optional<JWTUserData> validateToken(String jwtToken) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.token);
            DecodedJWT jwt = JWT.require(algorithm).build().verify(jwtToken);

            String role = jwt.getClaim("role").asString();
            return Optional.of(JWTUserData.builder()
                    .userId(jwt.getClaim("idUsuario").asLong())
                    .email(jwt.getSubject())
                    .role(role)
                    .build());

        } catch (JWTVerificationException exception) {
            return Optional.empty();
        }
    }
}
