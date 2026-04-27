package mensajes;

import java.io.Serializable;

// Utilizado de forma genérica en todas las desconexiones
public class Desconexion extends Mensaje {

    public Desconexion(String sender, String receiver) {
        super(TipoMensaje.DESCONEXION, sender, receiver);
    }

    @Override
    public Serializable getContent() {
        throw new UnsupportedOperationException("Este tipo de mensaje no tiene contenido");
    }

}
