package new_projetct.forun_hub.domain.usuario;

public record DadosListagemUsuario(
        Long id,
        String nomeCompleto,
        String nomeUsuario,
        String email,
        String biografia,
        String miniBiografia) {


    public DadosListagemUsuario(Usuario usuario){
        this(usuario.getId(), usuario.getNomeCompleto(), usuario.getNomeUsuario(),
                usuario.getEmail(), usuario.getBiografia(), usuario.getMiniBiografia());
    }

}
