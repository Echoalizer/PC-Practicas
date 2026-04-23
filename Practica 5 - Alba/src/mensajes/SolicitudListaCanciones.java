package mensajes;

public class SolicitudListaCanciones extends Mensaje {
    public SolicitudListaCanciones() {
        super(TipoMensaje.SOLICITUD_LISTA_CANCIONES);
    }

    @Override
    public TipoMensaje getTipo() {
        return TipoMensaje.SOLICITUD_LISTA_CANCIONES;
    }
}
