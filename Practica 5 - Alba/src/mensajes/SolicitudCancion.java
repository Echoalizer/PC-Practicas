package mensajes;

import java.io.Serializable;

public class SolicitudCancion extends Mensaje {

    public SolicitudCancion(String sender, String receiver) {
        super(TipoMensaje.SOLICITUD_CANCION, sender, receiver);
    }

    @Override
    public Serializable getContent() {
        throw new UnsupportedOperationException("Este tipo de mensaje no tiene contenido");
    }

}
