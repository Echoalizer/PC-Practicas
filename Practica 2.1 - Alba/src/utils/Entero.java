package utils;

import java.util.concurrent.atomic.AtomicInteger;

public class Entero {
	private AtomicInteger _valor;
	
	public Entero(int valor){
		_valor = new AtomicInteger(valor);
	}
	
	public int get_valor() {
		return _valor.get();
	}
	
	public void incrementar() {
		_valor.getAndIncrement();
	}
	
	public void decrementar() {
		_valor.getAndDecrement();
	}
}
