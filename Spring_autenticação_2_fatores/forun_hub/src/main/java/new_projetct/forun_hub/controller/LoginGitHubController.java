package new_projetct.forun_hub.controller;

import new_projetct.forun_hub.domain.autentication.github.LoginGitHubService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/login/github")
public class LoginGitHubController {
    
    private final LoginGitHubService loginGitHubService;

    public LoginGitHubController(LoginGitHubService loginGitHubService) {
        this.loginGitHubService = loginGitHubService;
    }


    @GetMapping
   public ResponseEntity<Void> redirecionarGitHub(){
       var url = loginGitHubService.gerarUrl();
       var headers = new HttpHeaders();
       headers.setLocation(URI.create(url));
       
       return new ResponseEntity<>(headers, HttpStatus.FOUND);
   }

    @GetMapping("/autorizado")
    public ResponseEntity<String> obterToken(@RequestParam String code){
        var token = loginGitHubService.obterToken(code);
        return ResponseEntity.ok(token);
    }

}
