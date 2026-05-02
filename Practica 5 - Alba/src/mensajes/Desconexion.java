package mensajes;

import utils.Usuario;

import java.io.Serializable;

// Utilizado de forma genérica en todas las desconexiones
public class Desconexion extends Mensaje {

    private final Usuario user;

    public Desconexion(String sender, String receiver, Usuario user) {
        super(TipoMensaje.DESCONEXION, sender, receiver);
        this.user = user;
    }

    @Override
    public Serializable getContent() {
        return this.user;
    }

}
