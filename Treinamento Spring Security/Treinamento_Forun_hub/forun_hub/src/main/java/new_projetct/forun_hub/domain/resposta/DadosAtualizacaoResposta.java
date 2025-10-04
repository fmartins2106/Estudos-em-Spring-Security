package new_projetct.forun_hub.domain.resposta;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoResposta(
        @NotNull(message = "Campo ID não pode ser vazio.")
        Long id,

        @NotBlank(message = "Digite uma mensagem.")
        String mensagem) {
}
