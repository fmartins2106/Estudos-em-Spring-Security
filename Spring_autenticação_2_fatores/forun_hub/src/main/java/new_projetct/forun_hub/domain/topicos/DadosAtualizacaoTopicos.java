package new_projetct.forun_hub.domain.topicos;

import new_projetct.forun_hub.domain.curso.Curso;

public record DadosAtualizacaoTopicos(
        String titulo,
        String mensagem,
        Curso curso) {
}
