package new_projetct.forun_hub.domain.autentication;

import jakarta.validation.constraints.NotBlank;

public record DadosTokenRefresh(
        @NotBlank
        String refreshToken) {
}
