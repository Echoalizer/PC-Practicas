package mensajes;

import java.io.Serializable;

public class RespuestaCancion extends Mensaje {

    public RespuestaCancion(String sender, String receiver) {
        super(TipoMensaje.RESPUESTA_CANCION_CC, sender, receiver);
    }

    @Override
    public Serializable getContent() {
        throw new UnsupportedOperationException("Este tipo de mensaje no tiene contenido");
    }

}
