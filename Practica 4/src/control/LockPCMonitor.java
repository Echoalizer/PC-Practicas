package control;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import util.Producto;

public class LockPCMonitor implements Almacen{
	
	private final Producto[] buffer; 
	private final ReentrantLock l;
	private int ini = 0;
	private int fin = 0;
	private int count = 0;
	private int N;
	private final Condition lleno;
	private final Condition vacio;
	
	
	public LockPCMonitor(int tamBuffer) {
		
		this.l = new ReentrantLock(true);
		this.N = tamBuffer;
		this.buffer = new Producto[tamBuffer];
		lleno = l.newCondition();
		vacio = l.newCondition();
		
	}
	
	@Override
	public void almacenar(Producto producto) throws InterruptedException {
		l.lock();
		
		while(count == N) 
			vacio.await();
		
		buffer[fin] = producto;
		fin = (fin + 1) % N;
		count++;
		lleno.signal();
		
		l.unlock();
	}

	@Override
	public Producto extraer() throws InterruptedException {
		l.lock();
		
		Producto p;
		while(count == 0)
			lleno.await();
		
		p = buffer[ini];
		ini = (ini + 1) % N;
		count--;
		vacio.signal();
		
		
		l.unlock();
		
		return p;
	}
	

}
