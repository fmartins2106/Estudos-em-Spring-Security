package new_projetct.forun_hub.controller;

import jakarta.validation.Valid;
import new_projetct.forun_hub.domain.resposta.*;
import new_projetct.forun_hub.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("respostas")
public class RespostaController {

    @Autowired
    private RespostaService respostaService;

    @PostMapping
    public ResponseEntity<DadosListagemRespostas> cadastrarResposta(@RequestBody @Valid DadosCadastroResposta dadosCadastroResposta,
                                                                    UriComponentsBuilder uriComponentsBuilder,
                                                                    @AuthenticationPrincipal Usuario autor){
        var resposta = respostaService.cadastrarResposta(dadosCadastroResposta, autor);
        var uri = uriComponentsBuilder.path("/respostas/{id}").buildAndExpand(resposta.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosListagemRespostas(resposta));
    }

    @PutMapping
    public ResponseEntity<DadosListagemRespostas> atualizar(@RequestBody @Valid DadosAtualizacaoResposta dadosAtualizacaoResposta){
        var resposta = respostaService.alterarResposta(dadosAtualizacaoResposta);
        return ResponseEntity.ok(new DadosListagemRespostas(resposta));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DadosListagemRespostas> marcarComoSolucionado(@PathVariable Long id){
        var resposta = respostaService.marcarComoSolucao(id);
        return  ResponseEntity.ok(new DadosListagemRespostas(resposta));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirResposta(@PathVariable Long id){
        respostaService.excluirResposta(id);
        return ResponseEntity.noContent().build();
    }

}
