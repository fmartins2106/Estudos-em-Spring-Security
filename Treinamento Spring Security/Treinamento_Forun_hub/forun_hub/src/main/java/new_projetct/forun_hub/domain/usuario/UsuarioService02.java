package new_projetct.forun_hub.domain.usuario;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Email;
import new_projetct.forun_hub.domain.perfil.Perfil;
import new_projetct.forun_hub.domain.perfil.PerfilNome;
import new_projetct.forun_hub.domain.perfil.PerfilRepository;
import new_projetct.forun_hub.infra.email.EmailService;
import new_projetct.forun_hub.infra.exception.ValidacaoRegraDeNegocio;
import new_projetct.forun_hub.infra.security.HierarquiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService02 {


    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private HierarquiaService hierarquiaService;

    @Transactional
    public Usuario pesquisaPorID(Long id){
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Erro, ID não encontrado."));
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
    public void ativarCodigoToken(Long id, String codigoToken){
        var usuario = usuarioRepository.findByToken(codigoToken)
                        .orElseThrow(() -> new RuntimeException("Erro código token inválido ou expirado."));
        usuario.verificadoTrue();
    }

    @Transactional
    public Usuario alterarDadosUsuario(Usuario logado, DadosEdiacaoUsuario dadosEdiacaoUsuario){
        return logado.alterarDados(dadosEdiacaoUsuario);
    }

    @Transactional
    public void alterarSenha(Usuario usuario, DadosAlteracaoSenha dadosAlteracaoSenha){
        if (!passwordEncoder.matches(usuario.getPassword(), dadosAlteracaoSenha.senhaAtual())){
            throw new ValidacaoRegraDeNegocio("Senha atual inválida.");
        }
        if (!dadosAlteracaoSenha.senha().equals(dadosAlteracaoSenha.confirmacaoNovaSenha())){
            throw new ValidacaoRegraDeNegocio("Confirmação de senha divergente.");
        }
        var senhaCriptografada = passwordEncoder.encode(dadosAlteracaoSenha.senha());
        usuario.alterarSenha(senhaCriptografada);
    }

    @Transactional
    public void desativarCadastro(Usuario logado, Long id){
        var usuario = pesquisaPorID(id);
        if (hierarquiaService.usuarioNaoTemPermissoes(logado, usuario, "ROLE_ADMINISTRADOR")){
            throw new AccessDeniedException("Sem permissão para executar este comando.");
        }
        usuario.desativado();
    }

    @Transactional
    public void adicionarPerfil(Long id, DadosPerfil dadosPerfil){
        var usuario = pesquisaPorID(id);
        var perfil = perfilRepository.findByNome(dadosPerfil.perfilNome());
        usuario.adicionarPerfil(perfil);
    }

    @Transactional
    public void reativarPerfil(Long id){
        var usuario = pesquisaPorID(id);
        usuario.reativarUsuario();
    }












}
