package mensajes;

import java.io.Serializable;

public class PreparadoSC extends Mensaje {

    private final Content contenido;

//    private final String address;

    public PreparadoSC(String sender, String receiver, String address, String id) {
        super(TipoMensaje.PREPARADO_SC, sender, receiver);
        this.contenido = new Content(address, id);
//        this.address = address;

    }

    @Override
    public Serializable getContent() {
        return this.contenido;
    }

}
