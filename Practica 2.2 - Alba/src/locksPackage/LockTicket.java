package locksPackage;

import java.util.concurrent.atomic.AtomicInteger;

import utils.Entero;

public class LockTicket implements LockId {
	
	private volatile int _next; //Ticket
	private AtomicInteger _numero; //Turno
	private final Entero[] _array; 
	
	
	
	public LockTicket(int N) {
		
		this._array = new Entero[N *2];
		for(int i = 0; i < N*2;i++) {
			this._array[i] = new Entero(0);
		}
		
		
		this._next = 1;
		this._numero = new AtomicInteger(1);
	}
	
	
	public void takeLock(int id) {
		
		//Ponemos en el array a la posicion de id, el nuevo valor 
		//de next + 1
		this._array[id].set_valor(this._numero.getAndAdd(1));
		
		//Mientras que no le toque al proceso con id => SE ESPERA
		while(this._array[id].get_valor() != this._next) {
			
		}
	}
	
	public void releaseLock(int id) {
		this._next += 1;
		this._array[id].set_valor(0);
		
	}
	
}
