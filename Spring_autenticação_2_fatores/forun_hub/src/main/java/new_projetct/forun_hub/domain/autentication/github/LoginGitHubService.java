package new_projetct.forun_hub.domain.autentication.github;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class LoginGitHubService {

    private static final String CLIENT_ID = "Ov23lieK70wNkrtURR7K"; // seu client_id real
    private static final String REDIRECT_URI = "http://localhost:8080/login/github/autorizado";
    private static final String CLIENT_SECRET = "208918dbc28947c66ff9f87ade29038ca4e55942";

    private static final String SCOPE = "read:user,user:email"; // <- corrigido

    private final RestClient restClient;

    public LoginGitHubService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    public String gerarUrl() {
        String encodedRedirect = URLEncoder.encode(REDIRECT_URI, StandardCharsets.UTF_8);
        String encodedScope = URLEncoder.encode(SCOPE, StandardCharsets.UTF_8);

        return "https://github.com/login/oauth/authorize"
                + "?client_id=" + CLIENT_ID
                + "&redirect_uri=" + encodedRedirect
                + "&scope=" + encodedScope;
    }


    public String obterToken(String code) {
        var resposta = restClient.post()
                .uri("https://github.com/login/oauth/access_token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .body("client_id=" + CLIENT_ID
                        + "&client_secret=" + CLIENT_SECRET
                        + "&code=" + code
                        + "&redirect_uri=" + REDIRECT_URI)
                .retrieve()
                .body(String.class);
        return resposta;
    }

}
