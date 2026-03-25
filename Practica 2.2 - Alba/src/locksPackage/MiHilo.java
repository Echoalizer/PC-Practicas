package locksPackage;

import utils.Entero;

public class MiHilo extends Thread {
	// 0 -> Decrementa 
			// 1 -> Incrementa
			private int _id; 
			private Entero _num;
			private int _numOperaciones;
			private LockBakery _lock;
			private int _operacion;
			
			public MiHilo(int id,int operacion, Entero num,int numOperaciones, LockBakery lock) {
				_id = id;
				_operacion = operacion;
				_num = num; // El numero compartido
				_numOperaciones = numOperaciones; //Para saber cuantas iteraciones van a ser
				_lock = lock;
				
			}
			
			@Override
			public void run() {
				
				for(int i = 0; i < _numOperaciones;i++) {
					_lock.takeLock(_id);
					
					if(_operacion == 0) {
						_num.decrementar();
					}
					else {
						_num.incrementar();
					}
					
					_lock.releaseLock(_id);
				}
			}
}
