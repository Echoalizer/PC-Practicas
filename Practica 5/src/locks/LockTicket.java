package locks;

import java.util.concurrent.atomic.AtomicInteger;

import util.Entero;

public class LockTicket implements LockId {
	private volatile int _next;  // Ticket
	private AtomicInteger _numero;  // Turno
	private final Entero[] _turnos;
	
	public LockTicket(int N) {
		this._turnos = new Entero[N];
		for(int i = 0; i < N; i++) {
			this._turnos[i] = new Entero();
		}

		this._next = 1;
		this._numero = new AtomicInteger(1);
	}
	
	@Override
	public void takeLock(int id) {
		// Ponemos en el array a la posicion de id, el nuevo valor de next + 1
		this._turnos[id].set_valor(this._numero.getAndAdd(1));
		
		// Mientras que no le toque al proceso con id => SE ESPERA
		while(this._turnos[id].get_valor() != this._next)
			;
	}

	@Override
	public void releaseLock(int id) {
		this._next += 1;
	}
}
