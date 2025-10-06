package new_projetct.forun_hub.domain.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DadosCadastroUsuario(
        @NotBlank(message = "Erro. Digite o nome completo.")
        @Pattern(regexp = "^[\\p{L}]+( [\\p{L}]+)+$")
        String nomeCompleto,

        @NotBlank(message = "Erro. Digite o nome do usuário completo.")
        String nomeUsuario,

        @NotBlank(message = "Erro. Digite o email completo.")
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$\n")
        String email,

        @NotBlank(message = "Erro. Digite uma senha.")
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*()_+{}\\[\\]:;\"'<>,.?/~`\\\\|-]).{8,}$\n",
        message = "Senha precisa contar uma letra maiuscula, um caracter e pelo menos 8 digitos.")
        String senha,
        String biografia,
        String miniBiografia) {
}
