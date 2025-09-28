package br.com.forum_hub.controller;

import br.com.forum_hub.domain.perfil.DadosPerfil;
import br.com.forum_hub.domain.usuario.DadosCadastroUsuario;
import br.com.forum_hub.domain.usuario.DadosListagemUsuario;
import br.com.forum_hub.domain.usuario.Usuario;
import br.com.forum_hub.domain.usuario.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class UsuarioController {


    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registrar")
    public ResponseEntity<DadosListagemUsuario> cadastrar(@RequestBody @Valid DadosCadastroUsuario dados, UriComponentsBuilder uriBuilder){
        var usuario = usuarioService.cadastrar(dados);
        var uri = uriBuilder.path("/{nomeUsuario}").buildAndExpand(usuario.getNomeUsuario()).toUri();
        return ResponseEntity.created(uri).body(new DadosListagemUsuario(usuario));
    }

    @GetMapping("/verificar-conta")
    public ResponseEntity<String> verificarEmail(@RequestParam String codigo){
        usuarioService.verificarEmail(codigo);
        return ResponseEntity.ok("Conta verificada com sucesso!");
    }


    @PatchMapping("adicionar-perfil/{id}")
    public ResponseEntity<DadosListagemUsuario> adicionarPerfil(@PathVariable Long id, @RequestBody @Valid DadosPerfil dados){
        var usuario = usuarioService.adicionarPerfil(id, dados);
        return ResponseEntity.ok(new DadosListagemUsuario(usuario));
    }


    @PatchMapping("remover-perfil/{id}")
    public ResponseEntity<DadosListagemUsuario> removerPerfil(@PathVariable Long id, @RequestBody @Valid DadosPerfil dados){
        var usuario = usuarioService.removerPerfil(id, dados);
        return ResponseEntity.ok(new DadosListagemUsuario(usuario));
    }


    @PatchMapping("/desativar/{id}")
    public ResponseEntity<Void> desativarUsuario(@PathVariable Long id, @AuthenticationPrincipal Usuario logado){
        usuarioService.desativarUsuario(id, logado);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/reativar-conta/{id}")
    public ResponseEntity<Void> reativarUsuario(@PathVariable Long id){
        usuarioService.reativarUsuario(id);
        return ResponseEntity.noContent().build();
    }

}





