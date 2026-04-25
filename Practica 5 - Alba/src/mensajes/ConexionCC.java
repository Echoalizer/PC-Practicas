package mensajes;

import java.io.Serializable;

public class ConexionCC extends Mensaje {

    public ConexionCC(String sender, String receiver) {
        super(TipoMensaje.CONEXION_CC, sender, receiver);
    }

    @Override
    public Serializable getContent() {
        throw new UnsupportedOperationException("Este tipo de mensaje no tiene contenido");
    }

}
