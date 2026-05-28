package mensajes;

import java.io.Serializable;

public class PreparadoCS extends Mensaje {

    private final Content content;

    public PreparadoCS(String sender, String receiver, String address, String id) {
        super(TipoMensaje.PREPARADO_CS, sender, receiver);
        this.content = new Content(address, id);
    }

    @Override
    public Serializable getContent() {
        return this.content;
    }

}
