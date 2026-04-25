package mensajes;

import java.io.Serializable;

public class SolicitudCancionCC extends Mensaje {

    public SolicitudCancionCC(String sender, String receiver) {
        super(TipoMensaje.SOLICITUD_CANCION_CC, sender, receiver);
    }

    @Override
    public Serializable getContent() {
        throw new UnsupportedOperationException("Este tipo de mensaje no tiene contenido");
    }

}
