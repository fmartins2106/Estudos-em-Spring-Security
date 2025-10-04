package new_projetct.forun_hub.domain.resposta;

import java.time.LocalDateTime;

public record DadosListagemRespostas(
        Long id,
        String mensagem,
        LocalDateTime dataCriacao,
        Boolean solucao,
        Long idCurso,
        String titulo) {

    public DadosListagemRespostas(Resposta resposta) {
        this(resposta.getId(), resposta.getMensagem(), resposta.getDataCriacao(), resposta.getSolucao()
        , resposta.getTopico().getId(), resposta.getTopico().getTitulo());
    }
}
