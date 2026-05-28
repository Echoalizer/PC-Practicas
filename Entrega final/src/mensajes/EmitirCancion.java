package mensajes;

import java.io.Serializable;

public class EmitirCancion extends Mensaje {

    private final Content content;

    public EmitirCancion(String sender, String receiver, String puerto, String id) {
        super(TipoMensaje.EMITIR_CANCION, sender, receiver);
        this.content = new Content(puerto, id);
    }

    @Override
    public Serializable getContent() {
        return this.content;
    }

}
