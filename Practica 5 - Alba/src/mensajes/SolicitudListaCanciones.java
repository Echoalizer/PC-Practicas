package mensajes;

import java.io.Serializable;

public class SolicitudListaCanciones extends Mensaje {

    public SolicitudListaCanciones(String sender, String receiver) {
        super(TipoMensaje.SOLICITUD_LISTA_CANCIONES, sender, receiver);
    }

    @Override
    public Serializable getContent() {
        throw new UnsupportedOperationException("Este tipo de mensaje no tiene contenido");
    }

}
