package new_projetct.forun_hub.domain.curso;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;


    public CursoService(CursoRepository repository) {
        this.cursoRepository = repository;
    }

    public Curso buscarPeloId(Long id){
        return cursoRepository.findById(id).orElseThrow(() ->
                new RuntimeException("ID não encontrado."));
    }

    public Page<DadosCurso> listar(Categoria categoria, Pageable paginacao){
        if (categoria != null){
            return cursoRepository.findByCategoriaAndAtivoTrue(categoria, paginacao).map(DadosCurso::new);
        }
        return cursoRepository.findAll(paginacao).map(DadosCurso::new);
    }

    public Curso atualizar(Long id, DadosAtualizacaoCurso dadosAtualizacaoCurso){
        Curso curso = buscarPeloId(id);
        curso.atualizar(dadosAtualizacaoCurso);
        return cursoRepository.save(curso);
    }

    public Curso cadastrarCurso(DadosCadastroCurso dadosCadastroCurso){
        Curso curso = new Curso(dadosCadastroCurso.nome(), dadosCadastroCurso.categoria());
        return cursoRepository.save(curso);
    }

    public void inativarCadastro(Long id){
        Curso curso = buscarPeloId(id);
        curso.inativarCadastro();
        cursoRepository.save(curso);
    }






}
