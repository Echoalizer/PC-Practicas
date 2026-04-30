package mensajes;

import utils.Usuario;

import java.io.Serializable;

public class Conexion extends Mensaje {

	private final Usuario user;

	public Conexion(String sender, String receiver, Usuario user) {
		super(TipoMensaje.CONEXION_CS, sender, receiver);
		this.user = user;
	}

	@Override
	public Serializable getContent() {
		return this.user;
	}

}
