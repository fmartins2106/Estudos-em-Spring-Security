//package new_projetct.forun_hub.domain.usuario;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.EqualsAndHashCode;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity(name = "Usuario")
//@Table(name = "usuarios")
//@Getter
//@AllArgsConstructor
//@NoArgsConstructor
//@EqualsAndHashCode(of = "id")
//public class Usuario {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    private String nomeCompleto;
//    private String email;
//    private String senha;
//    private String nomeUsuario;
//    private String biografia;
//    private String miniBiografia;
//    private Boolean verificado;
//    private String token;
//    private LocalDateTime expiracaoToken;
//    private Boolean ativo;
//    private List<Perfil> perfis = new ArrayList<>();
//
//
//}
