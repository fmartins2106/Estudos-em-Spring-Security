package new_projetct.forun_hub.domain.perfil;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.GrantedAuthority;

@Entity(name = "Perfil")
@Table(name = "perfil")
public class Perfil implements GrantedAuthority {
    // Implementa GrantedAuthority para que cada perfil do usuário
    // seja reconhecido pelo Spring Security como uma permissãoo válida,
    // permitindo que o framework controle o acesso automaticamente.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private PerfilNome nome;

    // Retorna a autoridade/permissão do perfil para o Spring Security.
    // Prefixo "ROLE_" é padrão do Spring para diferenciar roles de outras authorities.
    // Exemplo: se nome = ADMIN, retorna "ROLE_ADMIN".
    @Override
    public String getAuthority() {
        return "ROLE_" +nome;
    }
}
