package mensajes;

import utils.Cancion;

import java.util.Set;

public class RespuestaListaCanciones extends Mensaje {

    private Set<Cancion> lista;

    public RespuestaListaCanciones(Set<Cancion> lista) {
        super(TipoMensaje.RESPUESTA_LISTA_CANCIONES);
        this.lista = lista;
    }

    public Set<Cancion> getLista() {
        return lista;
    }

}
