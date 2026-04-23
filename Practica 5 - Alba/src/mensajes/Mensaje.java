package mensajes;

import java.io.Serializable;

public abstract class Mensaje implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TipoMensaje TipoMensaje;

	public Mensaje(TipoMensaje msg) {
		this.TipoMensaje = msg;
	}

	public TipoMensaje getTipo() {
		return this.TipoMensaje;
	}
	
}
