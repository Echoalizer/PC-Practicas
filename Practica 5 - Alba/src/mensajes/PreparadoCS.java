package mensajes;

import java.io.Serializable;

public class PreparadoCS extends Mensaje {

    public PreparadoCS(String sender, String receiver) {
        super(TipoMensaje.PREPARADO_CS, sender, receiver);
    }

    @Override
    public Serializable getContent() {
        throw new UnsupportedOperationException("Este tipo de mensaje no tiene contenido");
    }

}
