package locks;

import util.Entero;

import java.util.concurrent.atomic.AtomicInteger;

public class LockTicket implements LockId {
	private volatile int _next;  // Ticket
	private AtomicInteger _numero;  // Turno

	public LockTicket() {
		this._next = 1;
		this._numero = new AtomicInteger(1);
	}
	
	@Override
	public void takeLock(int id) {
		Entero turno = new Entero();
		// Ponemos en el valor, el nuevo valor de next + 1
		turno.set_valor(this._numero.getAndAdd(1));
		
		// Mientras que no le toque al proceso con id => SE ESPERA
		while (turno.get_valor() != this._next)
			;
	}

	@Override
	public void releaseLock(int id) {
		this._next += 1;
	}
}
