package new_projetct.forun_hub.domain.resposta;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import new_projetct.forun_hub.domain.topicos.Topico;

import java.time.LocalDateTime;

public record DadosCadastroResposta(
        @NotBlank(message = "Digite uma mensagem.")
        String mensagem,

        @NotBlank
        @Future(message = "Campo data não pode ter data inferior a data de hoje.")
        LocalDateTime dataCriacao,


        Boolean solucao,

        @NotNull
        Topico idTopico) {
}
