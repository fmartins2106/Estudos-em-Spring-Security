package new_projetct.forun_hub.domain.autentication;

import jakarta.transaction.Transactional;
import new_projetct.forun_hub.domain.usuario.Usuario;
import new_projetct.forun_hub.domain.usuario.UsuarioRepository;
import new_projetct.forun_hub.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AutenticacaoService {

    @Autowired
    TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository;


    public DadosTokenJWT autenticar(DadosLogin dadosLogin){
        var authenticationToken = new UsernamePasswordAuthenticationToken(dadosLogin.email(), dadosLogin.senha());
        var authentication = authenticationManager.authenticate(authenticationToken);
        var tokenAcesso = tokenService.gerarToken((Usuario) authentication.getPrincipal());
        var refreshTokenJWT = tokenService.gerarRefreshToken((Usuario) authentication.getPrincipal());
        return new DadosTokenJWT(tokenAcesso, refreshTokenJWT);
    }

    @Transactional
    public DadosTokenJWT atualizar(DadosTokenRefresh dadosTokenRefresh){
        var refreshToken = dadosTokenRefresh.refreshToken();
        Long idUsuario = Long.valueOf(tokenService.getSubject(refreshToken));
        var usuario = usuarioRepository.findById(idUsuario).orElseThrow();
        var acessoToken = tokenService.gerarToken(usuario);
        var tokenAtualizado = tokenService.gerarRefreshToken(usuario);
        return new DadosTokenJWT(acessoToken, tokenAtualizado);
    }


}
