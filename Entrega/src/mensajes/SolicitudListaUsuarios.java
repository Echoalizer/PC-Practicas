package mensajes;

import java.io.Serializable;

public class SolicitudListaUsuarios extends Mensaje {

    public SolicitudListaUsuarios(String sender, String receiver) {
        super(TipoMensaje.SOLICITUD_LISTA_USUARIOS, sender, receiver);
    }

    @Override
    public Serializable getContent() {
        throw new UnsupportedOperationException("Este tipo de mensaje no tiene contenido");
    }

}
