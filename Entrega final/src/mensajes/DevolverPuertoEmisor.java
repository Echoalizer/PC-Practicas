package mensajes;

import java.io.Serializable;

public class DevolverPuertoEmisor extends Mensaje {

    public DevolverPuertoEmisor(String sender, String receiver) {
        super(TipoMensaje.DEVOLVER_PUERTO_EMISOR, sender, receiver);
    }

    @Override
    public Serializable getContent() {
        throw new UnsupportedOperationException("Este tipo de mensaje no tiene contenido");
    }

}
