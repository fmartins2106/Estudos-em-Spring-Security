package new_projetct.forun_hub.domain.topicos;

import org.springframework.data.jpa.domain.Specification;

public class TopicoSpecification {

    public static Specification<Topico> temCategoria(String categoria) {
        return (root, query, builder) ->
                categoria == null
                        ? builder.conjunction()
                        : builder.equal(root.get("categoria"), categoria);
    }

    public static Specification<Topico> temIdCurso(Long idCurso) {
        return (root, query, builder) ->
                idCurso == null
                        ? builder.conjunction()
                        : builder.equal(root.join("curso").get("id"), idCurso);
    }

    public static Specification<Topico> estaAberto() {
        return (root, query, builder)
                -> builder.isTrue(root.get("aberto"));
    }

    public static Specification<Topico> estaSemResposta(Boolean semResposta) {
        return (root, query, builder) ->
                semResposta != null && semResposta
                        ? builder.equal(root.get("status"), Status.NAO_RESPONDIDO)
                        : builder.conjunction();
    }

    public static Specification<Topico> estarSolucionado(Boolean solucionado) {
        return (root, query, builder) ->
                solucionado != null && solucionado
                        ? builder.equal(root.get("status"), Status.RESOLVIDO)
                        : builder.conjunction();
    }



}
