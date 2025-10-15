package new_projetct.forun_hub.domain.usuario;

import jakarta.validation.constraints.NotNull;
import new_projetct.forun_hub.domain.perfil.PerfilNome;

public record DadosPerfil(
        @NotNull
        PerfilNome perfilNome) {
}
