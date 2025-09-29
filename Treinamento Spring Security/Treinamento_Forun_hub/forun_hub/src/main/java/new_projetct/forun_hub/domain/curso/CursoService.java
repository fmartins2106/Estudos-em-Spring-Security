package new_projetct.forun_hub.domain.curso;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;


    public CursoService(CursoRepository repository) {
        this.cursoRepository = repository;
    }

    @Transactional
    public Curso buscarPeloId(Long id){
        return cursoRepository.findById(id).orElseThrow(() ->
                new RuntimeException("ID não encontrado."));
    }

    @Transactional
    public Page<DadosCurso> listar(Categoria categoria, Pageable paginacao){
        if (categoria != null){
            return cursoRepository.findByCategoriaAndAtivoTrue(categoria, paginacao).map(DadosCurso::new);
        }
        return cursoRepository.findByAtivoTrue(paginacao).map(DadosCurso::new);
    }

    @Transactional
    public Curso atualizar(Long id, DadosAtualizacaoCurso dadosAtualizacaoCurso){
        Curso curso = buscarPeloId(id);
        curso.atualizar(dadosAtualizacaoCurso);
        return cursoRepository.save(curso);
    }

    @Transactional
    public Curso cadastrarCurso(DadosCadastroCurso dadosCadastroCurso){
        Curso curso = new Curso(dadosCadastroCurso.nome(), dadosCadastroCurso.categoria());
        return cursoRepository.save(curso);
    }

    @Transactional
    public void inativarCadastro(Long id){
        Curso curso = buscarPeloId(id);
        curso.inativarCadastro();
        cursoRepository.save(curso);
    }

}
