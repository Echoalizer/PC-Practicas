package mensajes;

import java.io.Serializable;

public class EmitirCancion extends Mensaje {

    public EmitirCancion(String sender, String receiver) {
        super(TipoMensaje.EMITIR_CANCION, sender, receiver);
    }

    @Override
    public Serializable getContent() {
        throw new UnsupportedOperationException("Este tipo de mensaje no tiene contenido");
    }

}
