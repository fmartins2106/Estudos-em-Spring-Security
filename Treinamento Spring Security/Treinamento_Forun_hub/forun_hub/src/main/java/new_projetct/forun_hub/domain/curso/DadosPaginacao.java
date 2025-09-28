package new_projetct.forun_hub.domain.curso;

import java.util.List;

public record DadosPaginacao<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages) {

}
