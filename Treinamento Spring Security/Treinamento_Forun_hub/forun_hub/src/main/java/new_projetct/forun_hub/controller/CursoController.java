package new_projetct.forun_hub.controller;

import jakarta.validation.Valid;
import new_projetct.forun_hub.domain.curso.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("cursos")
public class CursoController {

    private final CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @GetMapping
    public ResponseEntity<DadosPaginacao<DadosCurso>> listarDadosCursos(
            @RequestParam(required = false) Categoria categoria,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {

        Page<DadosCurso> pagina = cursoService.listar(categoria, pageable);

        DadosPaginacao<DadosCurso> dto = new DadosPaginacao<>(
                pagina.getContent(),
                pagina.getNumber(),
                pagina.getSize(),
                pagina.getTotalElements(),
                pagina.getTotalPages()
        );

        return ResponseEntity.ok(dto);
    }


    @GetMapping("/{id}")
    public ResponseEntity<DadosCurso> buscarPorID(@PathVariable Long id){
        Curso curso = cursoService.buscarPeloId(id);
        return ResponseEntity.ok(new DadosCurso(curso));
    }

    @PostMapping
    public ResponseEntity<DadosCurso> cadastrarCurso(@RequestBody @Valid DadosCadastroCurso dadosCadastroCurso, UriComponentsBuilder uriComponentsBuilder){
        Curso salvo = cursoService.cadastrarCurso(dadosCadastroCurso);
        var uri = uriComponentsBuilder.path("/cursos/{id}").buildAndExpand(salvo.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosCurso(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DadosCurso> atualizarDadosCurso(@PathVariable Long id,
                                                          @RequestBody @Valid DadosAtualizacaoCurso dadosAtualizacaoCurso){
        Curso atualizado = cursoService.atualizar(id, dadosAtualizacaoCurso);
        return ResponseEntity.ok(new DadosCurso(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativarCadastro(@PathVariable Long id){
        cursoService.inativarCadastro(id);
        return ResponseEntity.noContent().build();
    }


}
