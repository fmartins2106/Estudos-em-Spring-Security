package new_projetct.forun_hub.domain.topicos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import new_projetct.forun_hub.domain.curso.Categoria;
import new_projetct.forun_hub.domain.curso.Curso;

import java.time.LocalDateTime;

@Entity(name = "Topico")
@Table(name = "topicos")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Topico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String titulo;

    @NotNull
    private String mensagem;
//    private Usuario usuario;

    @NotNull
    private LocalDateTime dataCriacao;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;

    @NotNull
    private Boolean aberto;

    @NotNull
    private Integer quantidadeRespostas;


    @NotNull
    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    @NotNull
    private Curso curso;


    public Topico(DadosCadastroTopico dadosCadastroTopico, Curso curso) {
        this.titulo = dadosCadastroTopico.titulo();
        this.mensagem = dadosCadastroTopico.mensagem();
        this.dataCriacao = LocalDateTime.now();
        this.status = Status.NAO_RESPONDIDO;
        this.aberto = true;
        this.quantidadeRespostas = 0;
        this.categoria = curso.getCategoria();
        this.curso = curso;
    }

    public void atualizarDadoTopico(DadosAtualizacaoTopicos dadosAtualizacaoTopicos, Curso novoCurso) {
        if (dadosAtualizacaoTopicos.titulo() != null){
            this.titulo = dadosAtualizacaoTopicos.titulo();
        }
        if (dadosAtualizacaoTopicos.mensagem() != null){
            this.mensagem = dadosAtualizacaoTopicos.mensagem();
        }
        if (dadosAtualizacaoTopicos.cursoID() != null ){
            this.curso = novoCurso;
        }
    }

    public void inativarTopico() {
        this.status = Status.RESOLVIDO;
    }
}
