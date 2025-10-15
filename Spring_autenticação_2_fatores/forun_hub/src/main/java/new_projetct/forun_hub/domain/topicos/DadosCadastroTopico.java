package new_projetct.forun_hub.domain.topicos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroTopico(
        @NotBlank(message = "Campo título não pode ser vazio.")
        String titulo,

        @NotBlank(message = "Campo mensagem não pode ser vazio.")
        String mensagem,

        @NotNull(message = "Campo ID curso não pode ser vazio.")
        Long cursoID) {
}
