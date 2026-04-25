package mensajes;

import utils.Cancion;

import java.util.HashSet;


public class RespuestaListaCanciones extends Mensaje {

    // concretamente HashSet porque es serializable
    private final HashSet<Cancion> lista;

    public RespuestaListaCanciones(String sender, String receiver, HashSet<Cancion> lista) {
        super(TipoMensaje.RESPUESTA_LISTA_CANCIONES, sender, receiver);
        this.lista = lista;
    }

    @Override
    public HashSet<Cancion> getContent() {
        return this.lista;
    }

}
