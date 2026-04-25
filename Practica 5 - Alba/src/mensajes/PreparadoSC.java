package mensajes;

import java.io.Serializable;

public class PreparadoSC extends Mensaje {

    public PreparadoSC(String sender, String receiver) {
        super(TipoMensaje.PREPARADO_SC, sender, receiver);
    }

    @Override
    public Serializable getContent() {
        throw new UnsupportedOperationException("Este tipo de mensaje no tiene contenido");
    }

}
