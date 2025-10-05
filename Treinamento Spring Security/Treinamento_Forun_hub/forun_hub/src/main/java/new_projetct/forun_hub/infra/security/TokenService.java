package new_projetct.forun_hub.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import new_projetct.forun_hub.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class TokenService {

    @Value("${api.token.autentication.secret}")
    private String secret;

    public String gerarToken(Usuario usuario){
        System.out.println(secret);
        try {
            var algoritimo = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("API FORUM_HUB")
                    .withSubject(usuario.getUsername())
                    .withExpiresAt(tempoExpiracao(120))
                    .sign(algoritimo);
        }catch (JWTCreationException exception){
            throw new RuntimeException("Erro ao gerar tokenJWT", exception);
        }
    }

    public String gerarRefreshToken(Usuario usuario){
        System.out.println(secret);
        try {
            var algoritimo = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("API FORUM_HUB")
                    .withSubject(usuario.getUsername())
                    .withExpiresAt(tempoExpiracao(120))
                    .sign(algoritimo);
        }catch (JWTCreationException exception){
            throw new RuntimeException("Erro ao gerar tokenJWT", exception);
        }
    }

    private Instant tempoExpiracao(Integer minutos) {
        return ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"))
                .plusMinutes(minutos)
                .toInstant();

    }

    public String getSubject(String tokenJWT){
        try {
            var algoritimo = Algorithm.HMAC256(secret);
            return JWT.require(algoritimo)
                    .withIssuer("API FORUM_HUB")
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        }catch (JWTVerificationException exception){
            throw new RuntimeException("Erro de verificação do TokenJWT.", exception);
        }
    }







}
