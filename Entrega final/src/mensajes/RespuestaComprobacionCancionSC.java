package mensajes;

import java.io.Serializable;

import utils.Cancion;

public class RespuestaComprobacionCancionSC extends Mensaje {

	Cancion c;
	
	public RespuestaComprobacionCancionSC(Cancion c, String sender, String receiver) {
		super(TipoMensaje.RESPUESTA_COMPROBACION_SC, sender, receiver);
		this.c = c;
	}

	@Override
	public Serializable getContent() {
		return this.c;
	}

}
