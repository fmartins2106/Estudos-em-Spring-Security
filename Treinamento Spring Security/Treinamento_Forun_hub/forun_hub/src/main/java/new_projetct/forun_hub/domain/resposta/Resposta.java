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

    public Resposta(DadosCadastroResposta dadosCadastroResposta, Topico topico) {
        this.mensagem = dadosCadastroResposta.mensagem();
        this.dataCriacao = LocalDateTime.now();
        this.solucao = false;
        this.topico = topico;
    }

    public void atualizarDadosResposta(DadosAtualizacaoResposta dadosAtualizacaoResposta) {
       this.mensagem = dadosAtualizacaoResposta.mensagem();
    }

    public Resposta marcarComoSolucao() {
        this.solucao = true;
        return this;
    }
}
