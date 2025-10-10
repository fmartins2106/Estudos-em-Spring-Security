package new_projetct.forun_hub.domain.usuario;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import new_projetct.forun_hub.domain.perfil.Perfil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity(name = "Usuario")
@Table(name = "usuarios")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_completo")
    private String nomeCompleto;
    private String email;
    private String senha;

    @Column(name = "nome_usuario")
    private String nomeUsuario;
    private String biografia;
    private String miniBiografia;
    private Boolean verificado;
    private String token;
    private LocalDateTime expiracaoToken;
    private Boolean ativo;

    // Define um relacionamento Many-to-Many entre a entidade Usuario e a entidade Perfil.
    // Isso significa que um usuário pode ter vários perfis, e cada perfil pode pertencer a vários usuários.
    @ManyToMany(fetch = FetchType.EAGER)
    // Define a tabela intermediária que vai armazenar o relacionamento entre usuários e perfis.
    // Nome da tabela no banco: "usuario_perfils"
    @JoinTable(name = "usuarios_perfis",// Define a coluna que referencia a entidade Usuario na tabela intermediária
            joinColumns = @JoinColumn(name = "usuario_id"),// Define a coluna que referencia a entidade Perfil na tabela intermediária
            inverseJoinColumns = @JoinColumn(name = "perfil_id"))
// Cria a lista que vai armazenar os perfis associados ao usuário
// .
// Inicializada como ArrayList para evitar NullPointerException.
    private List<Perfil> perfis = new ArrayList<>();

    public Usuario(DadosCadastroUsuario dadosCadastroUsuario, String senhaCriptografada, Perfil perfil) {
        this.nomeCompleto = dadosCadastroUsuario.nomeCompleto();
        this.email = dadosCadastroUsuario.email();
        this.senha = senhaCriptografada;
        this.nomeUsuario = dadosCadastroUsuario.nomeUsuario();
        this.biografia = dadosCadastroUsuario.biografia();
        this.miniBiografia = dadosCadastroUsuario.miniBiografia();
        this.verificado = false;
        this.token = UUID.randomUUID().toString();
        this.expiracaoToken = LocalDateTime.now().plusMinutes(5);
        this.ativo = false;
        this.perfis.add(perfil);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return perfis;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public void verificadoTrue() {
        this.verificado = true;
        this.ativo = true;
    }

    public Usuario alterarDados(DadosEdicaoUsuario dadosEdicaoUsuario) {
        if (dadosEdicaoUsuario.nomeUsuario() != null){
            this.nomeUsuario = dadosEdicaoUsuario.nomeUsuario();
        }
        if (dadosEdicaoUsuario.biografia() != null){
            this.biografia = dadosEdicaoUsuario.biografia();
        }
        if (dadosEdicaoUsuario.miniBiografia() != null){
            this.miniBiografia = dadosEdicaoUsuario.miniBiografia();
        }
        return this;
    }

    public void alterarSenha(String senhaCriptografada) {
        this.senha = senhaCriptografada;
    }

    public void desativado() {
        this.ativo = false;
    }

    public void adicionarPerfil(Perfil perfil) {
        this.perfis.add(perfil);
    }

    public void reativarUsuario() {
        this.ativo = true;
    }
}
