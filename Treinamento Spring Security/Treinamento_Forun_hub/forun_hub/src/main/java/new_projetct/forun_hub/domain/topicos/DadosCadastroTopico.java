package new_projetct.forun_hub.domain.topicos;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import new_projetct.forun_hub.domain.curso.Categoria;
import new_projetct.forun_hub.domain.curso.Curso;

import java.time.LocalDateTime;

public record DadosCadastroTopico(
        @NotBlank(message = "Campo título não pode ser vazio.")
        String titulo,

        @NotBlank(message = "Campo mensagem não pode ser vazio.")
        String mensagem,

        @NotNull(message = "Campo ID curso não pode ser vazio.")
        Long cursoID) {
}
