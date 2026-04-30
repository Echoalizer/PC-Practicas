package locks;


public class LockBakery implements LockId {
    private final Entero[] _turno;

    public LockBakery(int N) {
        _turno = new Entero[N];
        for (int i = 0; i < N; i++)
            _turno[i] = new Entero();
    }

    public void takeLock(int id) {
        _turno[id].set_valor(1);
        int n = _turno.length;

        // Se va a buscar el valor real, que es el valor más alto hasta ahora en el array + 1
        int max = 0;
        for (int i = 0; i < n; i++) {
            if (max < _turno[i].get_valor())
                max = _turno[i].get_valor();
        }
        _turno[id].set_valor(max + 1);

        for (int i = 0; i < n; i++) {
            if (i == id) continue;

            // Cuando el valor de i no es 0 (lo que va siendo que ese hueco esta ocupado)
            // Y se cumple que
            //					1. Que el turno de i va a llegar antes que el de id ó
            //					2. Ambos tienen el mismo turno pero el hilo i tiene un id más pequeño que el hilo id
            //					   Se puede dar _turno[i] == _turno[id], si ambos hilos cogen turno a la misma vez (condicion de carrera)
            while (_turno[i].get_valor() != 0 && (_turno[i].get_valor() < _turno[id].get_valor() || (_turno[i].get_valor() == _turno[id].get_valor() && i < id)))
                ;  // espera activa
        }
    }

    public void releaseLock(int id) {
        _turno[id].set_valor(0);
    }
}
