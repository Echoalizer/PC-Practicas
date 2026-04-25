package mensajes;

import java.io.Serializable;

public class DesconexionCC extends Mensaje {

    public DesconexionCC(String sender, String receiver) {
        super(TipoMensaje.DESCONEXION_CC, sender, receiver);
    }

    @Override
    public Serializable getContent() {
        throw new UnsupportedOperationException("Este tipo de mensaje no tiene contenido");
    }

}
