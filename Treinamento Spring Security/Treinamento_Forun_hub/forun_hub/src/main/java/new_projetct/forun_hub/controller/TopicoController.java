package new_projetct.forun_hub.controller;

import jakarta.validation.Valid;
import new_projetct.forun_hub.domain.topicos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("topicos")
public class TopicoController {

    @Autowired
    private TopicoService topicoService;



    @PostMapping
    public ResponseEntity<DadosDetalhamentoTopico> cadastrar(@RequestBody @Valid DadosCadastroTopico dadosCadastroTopico, UriComponentsBuilder uriComponentsBuilder){
        var topico = topicoService.cadastrarTopico(dadosCadastroTopico);
        var uri = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoTopico(topico));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoTopico> atualizarDadosTopico(@PathVariable Long id, @RequestBody @Valid DadosAtualizacaoTopicos dadosAtualizacaoTopicos){
        var topico = topicoService.atualizarDadosTopico(id, dadosAtualizacaoTopicos);
        return ResponseEntity.ok().body(new DadosDetalhamentoTopico(topico));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativarTopico(@PathVariable Long id){
        topicoService.excluirTopico(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/solucionado/{id}")
    public ResponseEntity<DadosDetalhamentoTopico> marcarTopicoComoSolucionado(@PathVariable Long id){
        var topico = topicoService.fecharTopico(id);
        return ResponseEntity.ok(new DadosDetalhamentoTopico(topico));
    }


    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoTopico> buscarTopicoPeloId(@PathVariable Long id){
        var topico = topicoService.pesquisaTopicoPorID(id);
        return ResponseEntity.ok(new DadosDetalhamentoTopico(topico));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemTopico>> listarTopicos(@RequestParam(required = false) Long idCurso,
                                                                       @RequestParam(required = false) String categoria,
                                                                       @RequestParam(required = false, name = "sem-Resposta") Boolean semResposta,
                                                                       @RequestParam(required = false) Boolean solucionados,
                                                                       @PageableDefault(size = 10, sort = {"dataCriacao"})
                                                                       Pageable paginacao){
        var pagina = topicoService.listaTopicos(categoria, idCurso, semResposta, solucionados, paginacao);
        return ResponseEntity.ok(pagina);
    }



}
