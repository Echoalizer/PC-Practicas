package mensajes;

import utils.Cancion;

import java.io.Serializable;

public class RespuestaCancion extends Mensaje {

    private final Cancion cancion;

    public RespuestaCancion(String sender, String receiver, Cancion cancion) {
        super(TipoMensaje.RESPUESTA_CANCION_CC, sender, receiver);
        this.cancion = cancion;
    }

    @Override
    public Serializable getContent() {
        return cancion;
    }

}
