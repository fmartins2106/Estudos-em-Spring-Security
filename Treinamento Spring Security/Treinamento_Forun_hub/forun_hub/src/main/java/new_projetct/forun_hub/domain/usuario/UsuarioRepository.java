package new_projetct.forun_hub.domain.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {


    Optional<Usuario> findByLogin(String username);

    Optional<Usuario> findByEmailIgnoreCaseSensitiveAndVerificadoTrue(String email);
}
