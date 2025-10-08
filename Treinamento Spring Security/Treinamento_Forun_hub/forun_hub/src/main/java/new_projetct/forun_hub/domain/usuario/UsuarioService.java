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
    public Usuario pesquisaPorID(Long id){
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Erro. ID não encontrado."));
    }

    @Transactional
    public Usuario pesquisaNomeUsuario(String nomeUsuario){
        return usuarioRepository.findByNomeUsuarioIgnoreSensitiveCaseAndAtivoTrue(nomeUsuario)
                .orElseThrow(() -> new RuntimeException("Nome de usuário inválido."));
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
    public Usuario adicionarPerfil(Usuario logado, Long id, DadosPerfil dadosPerfil) {
        var usuario = pesquisaPorID(id);

        // Chama o método específico para adicionar perfil → apenas admin
        if (hierarquiaService.usuarioNaoTemPermissoesAddPerfil(logado, "ROLE_ADMINISTRADOR")) {
            throw new AccessDeniedException("Erro. Somente administradores podem adicionar perfis.");
        }

        var perfil = perfilRepository.findByNome(dadosPerfil.perfilNome());
        usuario.adicionarPerfil(perfil);
        return usuario;
    }

    @Transactional
    public void inativarPerfil(Usuario logado, Long id) {
        var usuario = pesquisaPorID(id);

        // Chama o método específico para inativar → admin ou próprio usuário
        if (hierarquiaService.usuarioNaoTemPermissoesInativar(logado, usuario, "ROLE_ADMINISTRADOR")) {
            throw new AccessDeniedException("Erro. Você não tem permissão para inativar este usuário.");
        }

        usuario.desativado();
    }



    @Transactional
    public void reativarUsuario(Usuario logado, Long id) {
        // Busca o usuário a ser reativado
        var usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        // Verifica se o usuário logado é administrador
        if (hierarquiaService.usuarioNaoTemPermissoesAddPerfil(logado, "ROLE_ADMINISTRADOR")) {
            throw new AccessDeniedException("Erro. Somente administradores podem reativar usuários.");
        }
        // Reativa o usuário
        usuario.reativarUsuario();
    }















}
