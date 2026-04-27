package mensajes;

import java.io.Serializable;

public class SolicitudCancion extends Mensaje {

    private final String cancionId;

    public SolicitudCancion(String sender, String receiver, String cancion) {
        super(TipoMensaje.SOLICITUD_CANCION, sender, receiver);
        this.cancionId = cancion;
    }

    @Override
    public Serializable getContent() {
        return this.cancionId;
    }

}
