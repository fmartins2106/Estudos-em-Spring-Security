package new_projetct.forun_hub.domain.curso;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroCurso(
        @NotBlank(message = "Campo nome não pode ser vazio.")
        String nome,
        @NotNull(message = "Campo categoria não pode ser vazio.")
        Categoria categoria) {
}
