package mensajes;

import java.io.Serializable;

public class Conexion extends Mensaje {

	public Conexion(String sender, String receiver) {
		super(TipoMensaje.CONEXION_CS, sender, receiver);
	}

	@Override
	public Serializable getContent() {
		throw new UnsupportedOperationException("Este tipo de mensaje no tiene contenido");
	}

}
