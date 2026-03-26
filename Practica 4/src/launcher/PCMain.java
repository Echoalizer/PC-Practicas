package launcher;

import java.util.ArrayList;
import java.util.List;

import PC.Almacen;
import PC.Consumidor;
import PC.Productor;
import PC.SyncPCMonitor;


public class PCMain {

	public static void main(String[] args) {
		int n = Integer.parseInt(args[0]);  // tamaño del buffer
		int p = Integer.parseInt(args[1]);  // num prods; cons
		int c = Integer.parseInt(args[2]);
		int it = Integer.parseInt(args[3]);  // num iteraciones
		
		
    	List<Thread> lista = new ArrayList<>();
    	
    	Almacen al = new SyncPCMonitor(n);
    	
    	for (int i = 0; i < p; i++) {
    		var prod = new Productor(i + 1, al, it);
    		lista.add(prod);
    		prod.start();
    	}
    	
        for (int i = 0; i < c; i++) {
        	var cons = new Consumidor(i + 1, al, it);
    		lista.add(cons);
    		cons.start();
        }
    	
    	
    	for(var i : lista) {
    		try {
				i.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
		System.out.println();  // comprobacion
	}

}
