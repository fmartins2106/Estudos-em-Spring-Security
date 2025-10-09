package new_projetct.forun_hub.domain.topicos;

import jakarta.transaction.Transactional;
import new_projetct.forun_hub.domain.curso.CursoService;
import new_projetct.forun_hub.domain.usuario.Usuario;
import new_projetct.forun_hub.infra.exception.ValidacaoRegraDeNegocio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class TopicoService {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoService cursoService;


    @Transactional
    public Page<DadosListagemTopico> listaTopicos(
            String categoria, Long idCurso, Boolean semResposta,
            Boolean solucionado, Pageable paginacao) {

        Specification<Topico> specification =
                (root, query,
                 builder) -> builder.conjunction();

        specification = specification
                .and(TopicoSpecification.estaAberto())
                .and(TopicoSpecification.temCategoria(categoria))
                .and(TopicoSpecification.temIdCurso(idCurso))
                .and(TopicoSpecification.estaSemResposta(semResposta))
                .and(TopicoSpecification.estarSolucionado(solucionado));

        return topicoRepository.findAll(specification, paginacao)
                .map(DadosListagemTopico::new);
    }




    public Topico pesquisaTopicoPorID(Long id){
        return topicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Erro. ID não encontrado."));
    }


    public Topico cadastrarTopico(DadosCadastroTopico dadosCadastroTopico, Usuario autor){
        var curso = cursoService.buscarPeloId(dadosCadastroTopico.cursoID());
        var topico = new Topico(dadosCadastroTopico, curso, autor);
        return topicoRepository.save(topico);
    }

    @Transactional
    public Topico atualizarDadosTopico(Long id ,DadosAtualizacaoTopicos dadosAtualizacaoTopicos){
        var topico = pesquisaTopicoPorID(id);
        topico.atualizarDadoTopico(dadosAtualizacaoTopicos);
        return topicoRepository.save(topico);
    }

    @Transactional
    public Topico fecharTopico(Long id){
        var topico = pesquisaTopicoPorID(id);
        topico.inativarTopico();
        return topico;
    }

    @Transactional
    public void excluirTopico(Long id){
        var topico = pesquisaTopicoPorID(id);
        if (topico.getStatus() == Status.NAO_RESPONDIDO){
            topicoRepository.deleteById(id);
            return;
        }
        throw new ValidacaoRegraDeNegocio("Erro. Exclusão de tópico só pode ser excluido se status = NAO_RESPONDIDO");
    }



}
