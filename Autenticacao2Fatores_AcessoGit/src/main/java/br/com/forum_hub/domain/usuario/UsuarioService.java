package br.com.forum_hub.domain.usuario;

import br.com.forum_hub.domain.perfil.DadosPerfil;
import br.com.forum_hub.domain.perfil.Perfil;
import br.com.forum_hub.domain.perfil.PerfilNome;
import br.com.forum_hub.domain.perfil.PerfilRepository;
import br.com.forum_hub.infra.email.EmailService;
import br.com.forum_hub.infra.exception.RegraDeNegocioException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PerfilRepository perfilRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmailIgnoreCaseAndVerificadoTrue(username)
                .orElseThrow(() -> new RuntimeException("Email não encontrado."));
    }

    @Transactional
    public Usuario cadastrar(DadosCadastroUsuario dados) {

        Optional<Usuario> optionalUsuario = usuarioRepository.findByEmailIgnoreCaseOrNomeUsuarioIgnoreCase(dados.email(), dados.nomeUsuario());

        if(optionalUsuario.isPresent()){
            throw new RegraDeNegocioException("Já existe uma conta cadastrada com esse email ou nome de usuário!");
        }


        if (!dados.senha().equals(dados.confirmacaoSenha())) {
            throw new RegraDeNegocioException("Senha não bate com a confirmação!");
        }

        var perfil = perfilRepository.findByNome(PerfilNome.ESTUDANTE);

        var senhaCriptografada = passwordEncoder.encode(dados.senha());
        var usuario = new Usuario(dados, senhaCriptografada, perfil);
        emailService.enviarEmailVerificacao(usuario);

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void verificarEmail(String codigo) {
        var usuario = usuarioRepository.findByToken(codigo).orElseThrow();
        usuario.verificar();

    }

    @Transactional
    public Usuario adicionarPerfil(Long id, DadosPerfil dados) {
        // Busca o usuário, lança exceção clara se não existir
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com id " + id));

        // Busca o perfil, lança exceção clara se não existir
        Perfil perfil = perfilRepository.findByNome(dados.perfilNome());
        if (perfil == null) {
            throw new RuntimeException("Perfil inválido: " + dados.perfilNome());
        }

        // Inicializa a lista de perfis caso esteja null
        if (usuario.getPerfis() == null) {
            usuario.setPerfis(new ArrayList<>());
        }

        // Adiciona o perfil
        usuario.adicionarPerfil(perfil);

        return usuario;
    }


    @Transactional
    public Usuario removerPerfil(Long id, DadosPerfil dados) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow();

        // Pega o perfil gerenciado do banco
        Perfil perfil = perfilRepository.findByNome(dados.perfilNome());

        if (perfil == null) {
            throw new RegraDeNegocioException("Perfil não encontrado");
        }

        usuario.getPerfis().remove(perfil); // remove usando a instância gerenciada
        return usuario;
    }

}
