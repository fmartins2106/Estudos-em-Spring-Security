package new_projetct.forun_hub.domain.autentication.github;

import org.springframework.boot.autoconfigure.web.client.RestClientBuilderConfigurer;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class LoginGitHubService {

    private static final String CLIENT_ID = "Ov23lieK70wNkrtURR7K"; // seu client_id real
    private static final String REDIRECT_URI = "http://localhost:8080/login/github/autorizado";
    private static final String CLIENT_SECRET = "208918dbc28947c66ff9f87ade29038ca4e55942";

    private static final String SCOPE = "read:user%20user:email"; // <- corrigido

    private final RestClient restClient;

    public LoginGitHubService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    public String gerarUrl() {
        return "https://github.com/login/oauth/authorize"
                + "?client_id=" + CLIENT_ID
                + "&redirect_uri=" + REDIRECT_URI
                + "&scope=" + SCOPE;
    }


    public String obterToken(String code) {
        var resposta = restClient.post()
                .uri("http://localhost:8080/login/oauth/access_token")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Map.of("code", code, "CLIENT_ID", CLIENT_ID,
                        "CLIENT_SECRET",CLIENT_SECRET,
                        "REDIRECT_URI", REDIRECT_URI))
                .retrieve()
                .body(String.class);
        return resposta;
    }
}
