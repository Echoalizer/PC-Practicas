package mensajes;

import java.io.Serializable;

public abstract class Mensaje implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private tipoMensaje tipoMensaje;
	
	public Mensaje(tipoMensaje msg) {
		this.tipoMensaje = msg;
	}
	
	public tipoMensaje getTipo() {
		return this.tipoMensaje;
	}
	
}
