package locks;

import util.Entero;

public class LockRompeEmpate implements LockId {
    private final Entero[] in;
    private final Entero[] last;

    public LockRompeEmpate(int N) {
        // Creamos dos arrays de enteros volatiles, y lo rellenamos llamando a su constructor
        in = new Entero[N];
        last = new Entero[N];
        for (int i = 0; i < N; i++) {
            in[i] = new Entero(-1);
            last[i] = new Entero();
        }
    }

    @Override
    public void takeLock(int id) {
        int n = in.length;  // in y length tienen el mismo tamaño; la matriz de procesos x etapas es cuadrada.
        for (int step = 0; step < n; step++) {
            in[id].set_valor(step);
            last[step].set_valor(id);
            for (int k = 0; k < n; k++)
                if (k != id) while ((in[k].get_valor() >= in[id].get_valor()) && (last[step].get_valor() == id))
                    ;  // bucle de espera activa -- hot standby
        }
    }

    @Override
    public void releaseLock(int id) {
        in[id].set_valor(-1);
    }
}
