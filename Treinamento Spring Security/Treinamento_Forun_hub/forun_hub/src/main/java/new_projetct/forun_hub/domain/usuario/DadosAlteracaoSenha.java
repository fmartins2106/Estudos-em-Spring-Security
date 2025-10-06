package new_projetct.forun_hub.domain.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DadosAlteracaoSenha(
        @NotBlank(message = "Erro. Digite sua senha atual.")
        String senhaAtual,
        @NotBlank(message = "Erro. Digite uma senha.")
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*()_+{}\\[\\]:;\"'<>,.?/~`\\\\|-]).{8,}$\n",
                message = "Senha precisa contar uma letra maiuscula, um caracter e pelo menos 8 digitos.")
        String senha,

        @NotBlank(message = "Digite novamente a senha")
        String confirmacaoNovaSenha) {

}
