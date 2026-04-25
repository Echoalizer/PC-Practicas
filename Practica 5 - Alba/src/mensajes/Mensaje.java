package mensajes;

import java.io.Serializable;

/**
 * Clase que representa los distintos tipos de mensajes a enviar por la aplicación
 */
public abstract class Mensaje implements Serializable {

	//	private static final long serialVersionUID = 1L;
	private final TipoMensaje TipoMensaje;

	public Mensaje(TipoMensaje msg) {
		this.TipoMensaje = msg;
	}

	public TipoMensaje getTipo() {
		return this.TipoMensaje;
	}
	
}
