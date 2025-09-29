package new_projetct.forun_hub.domain.topicos;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import new_projetct.forun_hub.domain.curso.Categoria;
import new_projetct.forun_hub.domain.curso.Curso;

import java.time.LocalDateTime;

public record DadosAtualizacaoTopicos(
        String titulo,
        String mensagem,
        Long cursoID) {
}
