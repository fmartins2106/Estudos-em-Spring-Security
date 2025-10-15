package new_projetct.forun_hub.domain.autentication;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DadosLogin(

        @NotBlank
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
        message = "Digite um email válido.")
        String email,
        // Regex para e-mail: // ^ e $ → delimitam início e fim da string.
        // [A-Za-z0-9._%+-]+ → parte local (antes do @) permite letras, números e símbolos comuns.
        // @ → separador entre usuário e domínio. // [A-Za-z0-9.-]+ → domínio (pode ter letras, números, pontos e hífens).
        // \\. → exige um ponto antes da extensão.
        // [A-Za-z]{2,} → pelo menos 2 letras na extensão (.com, .br, .org, etc.).

        @NotBlank
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*()_+{}\\[\\]:;\"'<>,.?/~`\\\\|-]).{8,}$",
        message = "Digite uma senha válida.")
        String senha) {
    // ^ e $ → indicam o início e o fim da string (garantem que a regex analise a string inteira).
    // (?=.*[A-Z]) → garante que existe pelo menos uma letra maiúscula.
    // (?=.*[!@#$%^&*()_+{}\\[\\]:;"'<>,.?/~`\\\\|\\-]) → garante que existe pelo menos um caractere especial.
    // .{8,} → exige mínimo de 8 caracteres no total.

}
