package mensajes;

import java.io.Serializable;

import utils.Cancion;

public class ActualizarCancReceptor extends Mensaje {

    private final Cancion cancion;
   
    
    public ActualizarCancReceptor(String sender, String receiver, Cancion cancion) {
        super(TipoMensaje.ACTUALIZAR_CANC_RECEPTOR, sender, receiver);
        this.cancion = cancion;
    }

    @Override
    public Serializable getContent() {
        return cancion;
    }
}
