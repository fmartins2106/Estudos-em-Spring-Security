package new_projetct.forun_hub.domain.topicos;

import java.time.LocalDateTime;

public record DadosListagemTopico(
        Long id,
        String titulo,
        String mensagem,
        Status status,
        LocalDateTime dataCriacao,
        Integer quantidadeRespostas,
        String curso) {

    public DadosListagemTopico(Topico topico) {
        this(topico.getId(), topico.getTitulo(), topico.getMensagem(), topico.getStatus(), topico.getDataCriacao(), topico.getQuantidadeRespostas(), topico.getCurso().getNome());
    }

}
