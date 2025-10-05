package new_projetct.forun_hub.infra.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import new_projetct.forun_hub.domain.usuario.Usuario;
import new_projetct.forun_hub.infra.exception.ValidacaoRegraDeNegocio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender enviadorEmail;

    private static final String EMAIL_ORIGEN = "fernandom.adm@gmail.com";
    private static final String NOME_ENVIADOR = "Forun Hub";
    public static final String URL_SITE = "http://localhost:8080";

    @Async
    private void enviarEmail(String emailUsuario, String assunto, String conteudo){
        MimeMessage message = enviadorEmail.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setFrom(EMAIL_ORIGEN, NOME_ENVIADOR);
            helper.setTo(emailUsuario);
            helper.setSubject(assunto);
            helper.setText(conteudo, true);
        }catch (MessagingException | UnsupportedEncodingException exception){
            throw new ValidacaoRegraDeNegocio("Erro ao enviar o email.");
        }

        enviadorEmail.send(message);
    }

    public void enviarEmailVerificacao(Usuario usuario){
        String assunto = "Aqui está seu link para verificar o email.";
        String conteudo = gerarConteudoEmail("Olá [[name]], <br>" +
                        "Por favor clieque no link abaixo para verificar seu conta <br>" +
                        "<h3> <a href=\"[[URL]]\" target=\"_self\">VERIFICAR</a></h3>" +
                        "Obrigado <br>" +
                        "Forun Hub :).", usuario.getNomeCompleto(), URL_SITE + "/verificar-conta?codigo="+usuario.getToken());
        enviarEmail(usuario.getUsername(), assunto, conteudo);
    }

    private String gerarConteudoEmail(String template, String nomeCompleto, String url) {
        return template.replace("[[name]]", nomeCompleto).replace("[[URL]]",url);
    }


}
