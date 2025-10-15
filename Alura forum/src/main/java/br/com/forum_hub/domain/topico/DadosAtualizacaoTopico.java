package br.com.forum_hub.domain.topico;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoTopico(
        @NotNull Long id,
        @NotBlank String titulo,
        @NotBlank String mensagem,
        @NotNull Long cursoId
) {
}
