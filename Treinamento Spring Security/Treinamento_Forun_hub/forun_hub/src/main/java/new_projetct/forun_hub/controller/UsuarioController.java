package new_projetct.forun_hub.controller;

import jakarta.validation.Valid;
import new_projetct.forun_hub.domain.usuario.*;
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
    public ResponseEntity<DadosListagemUsuario> cadastrarUsuario(@RequestBody @Valid DadosCadastroUsuario dadosCadastroUsuario,
                                                                 UriComponentsBuilder uriComponentsBuilder){
        var usuario = usuarioService.cadastrarUsuario(dadosCadastroUsuario);
        var uri = uriComponentsBuilder.path("/nomeUsuario}").buildAndExpand(usuario.getNomeUsuario()).toUri();
        return ResponseEntity.created(uri).body(new DadosListagemUsuario(usuario));
    }

    @GetMapping("/verificar-conta")
    public ResponseEntity<String> verificarEmail(@RequestParam String codigo){
        usuarioService.verificarEmail(codigo);
        return ResponseEntity.ok("Conta verificada com sucesso.");
    }

    @GetMapping("/{nomeUsuario}")
    public ResponseEntity<DadosListagemUsuario> exibirPerfil(@PathVariable String nomeUsuario){
        var usuario = usuarioService.pesquisaNomeUsuario(nomeUsuario);
        return ResponseEntity.ok(new DadosListagemUsuario(usuario));
    }

    @PatchMapping("/editar-perfil")
    public ResponseEntity<DadosListagemUsuario> editarCadastro(@RequestBody @Valid DadosEdicaoUsuario dadosEdicaoUsuario,
                                                               @AuthenticationPrincipal Usuario logado){
        var usuario = usuarioService.editarDadosUsuario(logado, dadosEdicaoUsuario);
        return ResponseEntity.ok(new DadosListagemUsuario(usuario));
    }

    @PatchMapping("alterar-senha")
    public ResponseEntity<Void> alterarSenha(@RequestBody @Valid DadosAlteracaoSenha dadosAlteracaoSenha,
                                             @AuthenticationPrincipal Usuario logado){
        usuarioService.alterarSenha(logado, dadosAlteracaoSenha);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/desativar/{id}")
    public ResponseEntity<Void> desativarCadastro(@AuthenticationPrincipal Usuario logado, @PathVariable Long id){
        usuarioService.inativarPerfil(logado, id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/reativar-conta/{id}")
    public ResponseEntity<Void> reativarUsuario(@PathVariable Long id, @AuthenticationPrincipal  Usuario logado){
        usuarioService.reativarUsuario(logado, id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("adicionar-perfil/{id}")
    public ResponseEntity<DadosListagemUsuario> adicionarPerfil(@PathVariable Long id, @AuthenticationPrincipal Usuario logado,
                                                @RequestBody @Valid DadosPerfil dadosPerfil){
        var usuario = usuarioService.adicionarPerfil(logado, id, dadosPerfil);
        return ResponseEntity.ok(new DadosListagemUsuario(usuario));
    }





}
