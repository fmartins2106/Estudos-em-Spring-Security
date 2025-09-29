package new_projetct.forun_hub.domain.resposta;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import new_projetct.forun_hub.domain.topicos.Topico;
import new_projetct.forun_hub.domain.usuario.Usuario;

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
    private String mensagem;
    private LocalDateTime dataCriacao;
    private Boolean solucao;
    private Topico topico;

}
