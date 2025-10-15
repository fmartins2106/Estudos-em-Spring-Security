package new_projetct.forun_hub.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import new_projetct.forun_hub.domain.autentication.*;
import new_projetct.forun_hub.domain.usuario.UsuarioRepository;
import new_projetct.forun_hub.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@SecurityRequirement(name = "bearer-key")
public class AutenticacaoController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AutenticacaoService autenticacaoService;


    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<DadosTokenJWT> efetuarLogin(@RequestBody @Valid DadosLogin dadosLogin){
        var dadosToken = autenticacaoService.autenticar(dadosLogin);
        return ResponseEntity.ok(dadosToken);
    }

    @PostMapping("/atualizar-token")
    public ResponseEntity<DadosTokenJWT> atualizarToken(@RequestBody @Valid DadosTokenRefresh dadosTokenRefresh){
        var dadosAtualizacaoToken = autenticacaoService.atualizar(dadosTokenRefresh);
        return ResponseEntity.ok(dadosAtualizacaoToken);
    }



}
