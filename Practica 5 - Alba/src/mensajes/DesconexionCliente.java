package mensajes;

import java.io.Serializable;

public class DesconexionCliente extends Mensaje {

	public DesconexionCliente(String sender, String receiver) {
		super(TipoMensaje.DESCONEXION_CS, sender, receiver);
	}

	@Override
	public Serializable getContent() {
		throw new UnsupportedOperationException("Este tipo de mensaje no tiene contenido");
	}

}
