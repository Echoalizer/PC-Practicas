package launcher;

import locksPackage.LockBakery;
import locksPackage.LockId;
import locksPackage.LockTicket;
import utils.EnteroConcurrente;

public class PruebaLibreria {
    public static void prueba(){
        int numNucleos = Runtime.getRuntime().availableProcessors();
		int aux;
		boolean aux2;
		
		aux = testLock(numNucleos,1000,new LockTicket(numNucleos));
		System.out.println("lock ticket: " + aux);
		aux = testLock(numNucleos,1000,new LockBakery(numNucleos));
		System.out.println("lock backery: " + aux);

    }

    private static int testLock(int numNucleos, int iteraciones, LockId lock) {
		Thread hilos[] = new Thread[numNucleos];
		EnteroConcurrente contador = new EnteroConcurrente(0);
		
		numNucleos = numNucleos - numNucleos%2; //siempre tiene que ser par
		
		for(int i = 0; i< numNucleos;i++) {
			int id = i;
			
			if(i%2 == 0) { //id par para restadores
				hilos[i] = new Thread(()->{
					for(int j = 0; j<iteraciones;j++) {
						lock.takeLock(id);
						contador.numero--;
						lock.releaseLock(id);
					}
				});
			}
			else { // id impar para sumadores
				hilos[i] = new Thread(()->{
					for(int j = 0; j<iteraciones;j++) {
						lock.takeLock(id);
						contador.numero++;
						lock.releaseLock(id);
					}
				});
			}
		}
		
		for(int i= 0; i< numNucleos; i++) {
			hilos[i].start();
		}
		
		for(int i= 0; i< numNucleos; i++) {
			try {
				hilos[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return contador.numero;
	}
	
}