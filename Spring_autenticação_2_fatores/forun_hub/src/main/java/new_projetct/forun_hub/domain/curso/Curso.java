package new_projetct.forun_hub.domain.curso;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Entity(name = "Curso")
@Table(name = "cursos")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotNull
    private String nome;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Categoria categoria;

    @NotNull
    private boolean ativo = true;

    public Curso(String nome, Categoria categoria) {
        this.nome = nome;
        this.categoria = categoria;
    }

    public void atualizar(@Valid DadosAtualizacaoCurso dadosAtualizacaoCurso) {
        this.nome = dadosAtualizacaoCurso.nome();
        this.categoria = dadosAtualizacaoCurso.categoria();
    }

    public void inativarCadastro() {
        this.ativo = false;
    }
}
