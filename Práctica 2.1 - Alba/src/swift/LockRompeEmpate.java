package swift;

public class LockRompeEmpate {

	private volatile boolean[] flag = new boolean[2]; // Indica si el proceso quiere entrar
	private volatile int last;// Indica a quién le toca entrar
	
	
	public LockRompeEmpate() {
		flag[0] = false; // Vamos a poner de primeras que ninguno de los dos procesos quiere entrar
		flag[1] = false;
		last = 1;
	}
	
	
	public void entrar(int id) {
		
		flag[id] = true;
		flag = flag;
		last = id;
		
		int otro = 0 ;
		if(id == 0)
			otro = 1;
		
		while(flag[otro] && last == id) {
			//Espera activa (busy wait)
		}
	}
	
	public void salir(int id) {
		flag[id] = false;
		flag = flag;
	}
	
	
}
