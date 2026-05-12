package com.project.fintrackApi.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.project.fintrackApi.domain.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.service}")
    private String secret;


    public String gerarToken(Usuario usuario){

        try{

            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withSubject(usuario.getEmail())
                    .withIssuer("fintrackApi")
                    .withClaim("id", usuario.getId())
                    .withClaim("nome", usuario.getNome())
                    .withExpiresAt(gerarExpiracao())
                    .sign(algorithm);
        }catch (Exception e){
            throw new RuntimeException("Erro ao Gerar Token JWT", e);
        }
    }

    public String validarToken(String token){
        try{

            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm)
                    .withIssuer("fintrackApi")
                    .build()
                    .verify(token)
                    .getSubject();
        }catch(JWTVerificationException e){
            return null;
        }
    }

    private Instant gerarExpiracao(){
        return LocalDateTime.now()
                .plusHours(24)
                .toInstant(ZoneOffset.of("-03:00"));
    }


}
