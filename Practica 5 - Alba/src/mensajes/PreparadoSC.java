package mensajes;

import java.io.Serializable;

public class PreparadoSC extends Mensaje {

    private final String address;

    public PreparadoSC(String sender, String receiver, String address) {
        super(TipoMensaje.PREPARADO_SC, sender, receiver);
        this.address = address;
    }

    @Override
    public Serializable getContent() {
        return this.address;
    }

}
