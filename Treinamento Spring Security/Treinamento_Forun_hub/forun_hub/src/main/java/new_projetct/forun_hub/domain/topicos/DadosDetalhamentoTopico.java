package new_projetct.forun_hub.domain.topicos;

import new_projetct.forun_hub.domain.curso.Curso;

public record DadosDetalhamentoTopico(
        Long id,
        String titulo,
        String mensagem,
        Long idCurso) {

    public DadosDetalhamentoTopico(Topico topico) {
        this(topico.getId(), topico.getTitulo(), topico.getMensagem()
        , topico.getCurso().getId());
    }
}
