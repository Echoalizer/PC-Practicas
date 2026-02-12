package swift;

import utils.Entero;

public class MiHilo extends Thread {
		// 0 -> Decrementa 
		// 1 -> Incrementa
		private int _id; 
		private Entero _num;
		private int _numOperaciones;
		private LockRompeEmpate _lock;
		
		public MiHilo(int id, Entero num,int numOperaciones, LockRompeEmpate lock) {
			_id = id;
			_num = num; // El numero compartido
			_numOperaciones = numOperaciones; //Para saber cuantas iteraciones van a ser
			_lock = lock;
			
		}
		
		@Override
		public void run() {
			
			for(int i = 0; i < _numOperaciones;i++) {
				_lock.entrar(_id);
				
				if(_id == 0) {
					_num.decrementar();
				}
				else {
					_num.incrementar();
				}
				
				_lock.salir(_id);
			}
		}
}
