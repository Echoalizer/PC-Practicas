package mensajes;

import java.io.Serializable;

/**
 * Clase que representa los distintos tipos de mensajes a enviar por la aplicación
 */
public abstract class Mensaje implements Serializable {

	//	private static final long serialVersionUID = 1L;

	// Header
	private final TipoMensaje TipoMensaje;
	private final String from;
	private final String to;

	// Contenido del mensaje -- para las clases que implementan
	private Serializable content;

	public Mensaje(TipoMensaje msg, String sender, String receiver) {
		this.TipoMensaje = msg;
		this.from = sender;
		this.to = receiver;
	}

	public Mensaje(TipoMensaje msg, String sender, String receiver, Serializable content) {
		this(msg, sender, receiver);
		this.content = content;
	}

	public TipoMensaje getTipo() {
		return this.TipoMensaje;
	}

	public String getSender() {
		return this.from;
	}

	public String getReceiver() {
		return this.to;
	}

	// depende del tipo concreto de mensaje
	public abstract Serializable getContent();

}
