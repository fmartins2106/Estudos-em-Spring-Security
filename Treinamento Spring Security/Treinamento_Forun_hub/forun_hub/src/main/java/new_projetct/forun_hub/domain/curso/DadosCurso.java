package new_projetct.forun_hub.domain.curso;

public record DadosCurso(
        Long id,
        String nome,
        Categoria categoria) {

    public DadosCurso(Curso curso) {
        this(curso.getId(), curso.getNome(), curso.getCategoria());
    }


}
