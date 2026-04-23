package mensajes;

import utils.Usuario;

import java.util.Set;


public class RespuestaListaUsuarios extends Mensaje {

    private Set<Usuario> lista;

    public RespuestaListaUsuarios() {
        super(TipoMensaje.RESPUESTA_LISTA_USUARIOS);
    }

    public Set<Usuario> getLista() {
        return lista;
    }

}
