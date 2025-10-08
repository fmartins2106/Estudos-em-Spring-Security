package new_projetct.forun_hub.domain.usuario;

import jakarta.transaction.Transactional;
import new_projetct.forun_hub.domain.perfil.PerfilNome;
import new_projetct.forun_hub.domain.perfil.PerfilRepository;
import new_projetct.forun_hub.infra.email.EmailService;
import new_projetct.forun_hub.infra.exception.ValidacaoRegraDeNegocio;
import new_projetct.forun_hub.infra.security.HierarquiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private HierarquiaService hierarquiaService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Erro. Login não encontrado."));
    }

    @Transactional
    public Usuario cadastrarUsuario(DadosCadastroUsuario dadosCadastroUsuario){
        var senhaCriptografada = passwordEncoder.encode(dadosCadastroUsuario.senha());
        var perfil = perfilRepository.findByNome(PerfilNome.ESTUDANTE);
        var usuario = new Usuario(dadosCadastroUsuario, senhaCriptografada, perfil);
        emailService.enviarEmailVerificacao(usuario);
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void verificarEmail(String codigo){
        var usuario = usuarioRepository.findByToken(codigo)
                .orElseThrow(() -> new RuntimeException("Token inválido ou expirado."));
        usuario.verificadoTrue();
    }

    @Transactional
    public Usuario editarDadosUsuario(Usuario usuario, DadosEdiacaoUsuario dadosEdiacaoUsuario){
        return usuario.alterarDados(dadosEdiacaoUsuario);
    }

    @Transactional
    public void alterarSenha(Usuario usuario,DadosAlteracaoSenha dadosAlteracaoSenha){
        if (!passwordEncoder.matches(dadosAlteracaoSenha.senhaAtual(), usuario.getPassword())){
            throw new ValidacaoRegraDeNegocio("Senha digitada não confere com senha Atual.");
        }
        if (!dadosAlteracaoSenha.confirmacaoNovaSenha().equals(dadosAlteracaoSenha.confirmacaoNovaSenha())){
            throw new ValidacaoRegraDeNegocio("Senha e confirmação não conferem.");
        }
        String senhaCriptografada = passwordEncoder.encode(dadosAlteracaoSenha.senha());
        usuario.alterarSenha(senhaCriptografada);
    }

    @Transactional
    public void desativarCadastro(Long id, Usuario logado){
        var usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ID usuário não encontrado."));
        if (hierarquiaService.usuarioNaoTemPermissoes(logado, usuario, "ROLE_ADMINISTRADOR")){
            throw new AccessDeniedException("Não é possivel realizar essa operação.");
        }
        usuario.desativado();
    }

    @Transactional
    public Usuario adicionarPerfil(Long id, DadosPerfil dadosPerfil){
        var usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ID não encontrado."));
        var perfil = perfilRepository.findByNome(dadosPerfil.perfilNome());
        usuario.adicionarPerfil(perfil);
        return usuario;
    }

    @Transactional
    public void reativarUsuario(Long id){
        var usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ID não encontrado."));
        usuario.reativarUsuario();
    }















}
