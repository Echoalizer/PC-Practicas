package mensajes;

import java.io.Serializable;

public class ConfirmacionActualizacionCanc extends Mensaje {

	public ConfirmacionActualizacionCanc(String sender, String receiver) {
		super(TipoMensaje.CONFIRMACION_ACTUALIZACION_CANC, sender, receiver);
		
	}

	@Override
	public Serializable getContent() {
        throw new UnsupportedOperationException("Este tipo de mensaje no tiene contenido");
	}

	

}
