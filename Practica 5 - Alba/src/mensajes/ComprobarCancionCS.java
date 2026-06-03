package mensajes;

import java.io.Serializable;

import utils.Cancion;

public class ComprobarCancionCS extends Mensaje {

	Cancion c;
	
	public ComprobarCancionCS(Cancion c, String sender, String receiver) {
		super(TipoMensaje.COMPROBAR_CANCION_CS, sender, receiver);
		this.c = c;
	}

	@Override
	public Serializable getContent() {
		return c;
	}
	
}
