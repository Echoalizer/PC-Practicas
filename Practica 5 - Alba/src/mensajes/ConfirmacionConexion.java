package mensajes;

import java.io.Serializable;

public class ConfirmacionConexion extends Mensaje {

    private int puerto;

    public ConfirmacionConexion(String sender, String receiver, int puerto) {
        super(TipoMensaje.CONFIRMACION_CONEXION, sender, receiver);
        this.puerto = puerto;
    }

    @Override
    public Serializable getContent() {
        return this.puerto;
    }

}
