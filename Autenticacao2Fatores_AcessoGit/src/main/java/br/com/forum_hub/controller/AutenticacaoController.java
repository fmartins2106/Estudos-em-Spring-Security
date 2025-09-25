package br.com.forum_hub.controller;

import br.com.forum_hub.domain.autenticacao.DadosLogin;
import br.com.forum_hub.domain.autenticacao.DadosRefreshToken;
import br.com.forum_hub.domain.autenticacao.DadosTokenJWT;
import br.com.forum_hub.domain.autenticacao.TokenService;
import br.com.forum_hub.domain.usuario.Usuario;
import br.com.forum_hub.domain.usuario.UsuarioRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("login")
public class AutenticacaoController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<DadosTokenJWT> efetuarLogin(@RequestBody @Valid DadosLogin dadosLogin){
       var authenticationToken = new UsernamePasswordAuthenticationToken(dadosLogin.email(), dadosLogin.senha());
       var authentication = authenticationManager.authenticate(authenticationToken);
       var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());
       String refreshToken = tokenService.gerarRefreshToken((Usuario) authentication.getPrincipal());
       return ResponseEntity.ok(new DadosTokenJWT(tokenJWT, refreshToken));
    }

    @PostMapping("/atualizar-token")
    public ResponseEntity<DadosTokenJWT> atualizarToken(@RequestBody @Valid DadosRefreshToken dadosRefreshToken){
        var refreshToken = dadosRefreshToken.refreshToken();
        Long idUsuario = Long.valueOf(tokenService.verificarToken(refreshToken));
        var usuario = usuarioRepository.findById(idUsuario).orElseThrow();

        var tokenAcesso = tokenService.gerarToken(usuario);
        String tokenDeAtualizacao = tokenService.gerarRefreshToken(usuario);

        return ResponseEntity.ok(new DadosTokenJWT(tokenAcesso, tokenDeAtualizacao));
    }



}
