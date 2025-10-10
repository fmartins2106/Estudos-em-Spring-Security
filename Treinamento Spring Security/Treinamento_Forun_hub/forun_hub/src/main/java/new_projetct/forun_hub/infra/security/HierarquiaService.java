// Pacote onde a classe está localizada (organização do projeto)
package new_projetct.forun_hub.infra.security;

// Importações de classes necessárias
import new_projetct.forun_hub.domain.usuario.Usuario; // Modelo de usuário do domínio
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy; // Gerencia hierarquia de papéis
import org.springframework.security.core.GrantedAuthority; // Representa uma permissão (ROLE_USER, ROLE_ADMIN, etc.)
import org.springframework.stereotype.Service;

import java.util.List;

// Anotação que indica que essa classe é um "Service" gerenciado pelo Spring
@Service
public class HierarquiaService {

    // Injeta automaticamente o componente que gerencia a hierarquia de papéis
    @Autowired
    RoleHierarchy roleHierarchy;

    // Para adicionar perfil → só administradores
    public boolean usuarioNaoTemPermissoesAddPerfil(Usuario logado, String perfilDesejado) {
        return logado.getAuthorities().stream()
                // Para cada autoridade, pega as autoridades alcançáveis
                .flatMap(autoridade ->
                        roleHierarchy.getReachableGrantedAuthorities(List.of(autoridade)).stream())
                // Verifica se alguma delas é o perfil desejado
                .noneMatch(perfil ->
                        perfil.getAuthority().equals(perfilDesejado));
    }

    // Para inativar conta → admin ou próprio usuário
    public boolean usuarioNaoTemPermissoesInativar(Usuario logado, Usuario autor, String perfilDesejado) {
        return logado.getAuthorities().stream()
                .flatMap(autoridade ->
                        roleHierarchy.getReachableGrantedAuthorities(List.of(autoridade))
                                .stream())
                // Verifica se alguma autoridade bate com o perfil desejado ou se é o próprio usuário
                .noneMatch(perfil ->
                        perfil.getAuthority().equals(perfilDesejado) || logado.getId().equals(autor.getId()));
    }
}
