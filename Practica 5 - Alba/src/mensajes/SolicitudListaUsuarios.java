package mensajes;

public class SolicitudListaUsuarios extends Mensaje {
    public SolicitudListaUsuarios() {
        super(TipoMensaje.SOLICITUD_LISTA_USUARIOS);
    }
}
