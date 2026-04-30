package mensajes;

import java.io.Serializable;

public class PreparadoCS extends Mensaje {

    private final String address;

    public PreparadoCS(String sender, String receiver, String address) {
        super(TipoMensaje.PREPARADO_CS, sender, receiver);
        this.address = address;
    }

    @Override
    public Serializable getContent() {
        return this.address;
    }

}
