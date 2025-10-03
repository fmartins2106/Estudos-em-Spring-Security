package new_projetct.forun_hub.domain.resposta;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import new_projetct.forun_hub.domain.topicos.Topico;

import java.time.LocalDateTime;

@Entity(name = "Resposta")
@Table(name = "respostas")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Resposta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    private Usuario Usuario;
    @NotNull
    private String mensagem;

    @NotNull
    private LocalDateTime dataCriacao;

    @NotNull
    private Boolean solucao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topico_id")
    private Topico topico;

    public Resposta(DadosCadastroResposta dadosCadastroResposta) {
        this.mensagem = mensagem;
        this.dataCriacao = LocalDateTime.now();
        this.solucao = solucao;
        this.topico = topico;
    }

    public void atualizarDadosResposta(DadosAtualizacaoResposta dadosAtualizacaoResposta) {
        if (dadosAtualizacaoResposta.mensagem() != null){
            this.mensagem = dadosAtualizacaoResposta.mensagem();
        }
        if (dadosAtualizacaoResposta.solucao() != null){
            this.solucao = dadosAtualizacaoResposta.solucao();
        }
        if (dadosAtualizacaoResposta.idTopico() != null && dadosAtualizacaoResposta.idTopico().getId() != null){
            this.topico = dadosAtualizacaoResposta.idTopico();
        }
    }
}
