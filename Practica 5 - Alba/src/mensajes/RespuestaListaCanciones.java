package mensajes;

import utils.Cancion;

import java.util.ArrayList;


public class RespuestaListaCanciones extends Mensaje {

    // concretamente ArrayList porque es serializable
    private final ArrayList<Cancion> lista;

    public RespuestaListaCanciones(String sender, String receiver, ArrayList<Cancion> lista) {
        super(TipoMensaje.RESPUESTA_LISTA_CANCIONES, sender, receiver);
        this.lista = lista;
    }

    @Override
    public ArrayList<Cancion> getContent() {
        return this.lista;
    }

}
