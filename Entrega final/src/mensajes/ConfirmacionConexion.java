package mensajes;

import java.io.Serializable;

public class ConfirmacionConexion extends Mensaje {

//    private final int puerto;

    public ConfirmacionConexion(String sender, String receiver) {
        super(TipoMensaje.CONFIRMACION_CONEXION, sender, receiver);
//        this.puerto = puerto;
    }

    @Override
    public Serializable getContent() {
//        return this.puerto;
        throw new UnsupportedOperationException("Este tipo de mensaje no tiene contenido");
    }

}
