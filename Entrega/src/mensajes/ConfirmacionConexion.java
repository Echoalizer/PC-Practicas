package mensajes;

import java.io.Serializable;

public class ConfirmacionConexion extends Mensaje {

    public ConfirmacionConexion(String sender, String receiver) {
        super(TipoMensaje.CONFIRMACION_CONEXION, sender, receiver);
    }

    @Override
    public Serializable getContent() {
        throw new UnsupportedOperationException("Este tipo de mensaje no tiene contenido");
    }

}
