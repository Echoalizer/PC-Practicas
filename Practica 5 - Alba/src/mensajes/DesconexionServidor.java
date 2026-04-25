package mensajes;

import java.io.Serializable;

public class DesconexionServidor extends Mensaje {

    public DesconexionServidor(String sender, String receiver) {
        super(TipoMensaje.DESCONEXION_SC, sender, receiver);
    }

    @Override
    public Serializable getContent() {
        throw new UnsupportedOperationException("Este tipo de mensaje no tiene contenido");
    }

}
