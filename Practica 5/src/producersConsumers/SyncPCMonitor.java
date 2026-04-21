package producersConsumers;

import util.Producto;

// Monitor Synchronized para Productores/Consumidores
// Synchronized -> | Metodos synchronized
//  			   | Variables privadas
//				   | wait / notifyAll()

public class SyncPCMonitor implements Almacen {
    private final Producto[] buffer;
    private final int N;
    private int ini = 0;
    private int fin = 0;
    private int count = 0;

    public SyncPCMonitor(int tamBuffer) {
        this.N = tamBuffer;
        this.buffer = new Producto[tamBuffer];
    }

    @Override
    public synchronized void almacenar(Producto producto) throws InterruptedException {
        while (count == N)
            wait();

        buffer[fin] = producto;
        fin = (fin + 1) % N;
        count++;
        notifyAll();
    }

    @Override
    public synchronized Producto extraer() throws InterruptedException {
        Producto p;
        while (count == 0)
            wait();
        p = buffer[ini];
        ini = (ini + 1) % N;
        count--;
        notifyAll();

        return p;
    }
}
