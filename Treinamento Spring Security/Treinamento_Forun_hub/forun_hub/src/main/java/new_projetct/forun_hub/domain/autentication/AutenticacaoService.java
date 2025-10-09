package new_projetct.forun_hub.domain.autentication;

import jakarta.transaction.Transactional;
import new_projetct.forun_hub.domain.usuario.Usuario;
import new_projetct.forun_hub.domain.usuario.UsuarioRepository;
import new_projetct.forun_hub.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AutenticacaoService {

    @Autowired
    TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository;


    @Transactional
    public DadosTokenJWT autenticar(DadosLogin dadosLogin){
        var authenticationToken = new UsernamePasswordAuthenticationToken(dadosLogin.email(), dadosLogin.senha());
        var authentication = authenticationManager.authenticate(authenticationToken);
        var tokenAcesso = tokenService.gerarToken((Usuario) authentication.getPrincipal());
        var refreshTokenJWT = tokenService.gerarRefreshToken((Usuario) authentication.getPrincipal());
        return new DadosTokenJWT(tokenAcesso, refreshTokenJWT);
    }


    public DadosTokenJWT atualizar(DadosTokenRefresh dadosTokenRefresh){
        // Recupera o refreshToken que foi enviado no objeto de entrada (dadosTokenRefresh)
        var refreshToken = dadosTokenRefresh.refreshToken();
        // Extrai o ID do usuário a partir do subject contido dentro do refreshToken
        var userName = tokenService.getSubject(refreshToken);
        // Busca o usuário no banco de dados pelo ID extraído do token
        // Caso o usuário não exista, lança uma exceção UsernameNotFoundException
        var usuario = usuarioRepository.findByEmail(userName)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));
        // Gera um novo token de acesso (access token) para o usuário
        var acessoToken = tokenService.gerarToken(usuario);
        // Gera um novo refresh token para o usuário
        var tokenAtualizado = tokenService.gerarRefreshToken(usuario);
        // Retorna um objeto DadosTokenJWT contendo o novo access token e o novo refresh token
        return new DadosTokenJWT(acessoToken, tokenAtualizado);
    }





}
