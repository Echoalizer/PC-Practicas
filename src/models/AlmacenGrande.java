package models;

import util.Producto;

import java.util.concurrent.Semaphore;

public class AlmacenGrande implements Almacen {
    private final int N;
    private final Producto[] buffer;  // usado como buffer circular
    private final Semaphore empty;
    private final Semaphore full = new Semaphore(0);
    private final Semaphore mutexC = new Semaphore(1);
    private final Semaphore mutexP = new Semaphore(1);
    private int ini = 0, fin = 0;

    public AlmacenGrande(int N) {
        this.N = N;
        this.buffer = new Producto[N];
        this.empty = new Semaphore(N);
    }

    @Override
    public void almacenar(Producto producto) {
        try {
            empty.acquire();
            mutexP.acquire();
            buffer[fin] = producto;
            fin = (fin + 1) % N;
            mutexP.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            full.release();  // si hay error metemos basura
        }
    }

    @Override
    public Producto extraer() {
        Producto ret = null;
        try {
            full.acquire();
            mutexC.acquire();
            ret = buffer[ini];  // null si estuviera vacío
            buffer[ini] = null;
            ini = (ini + 1) % N;
            mutexC.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            empty.release();  // si hay error perdemos el dato
        }
        return ret;
    }

    public void checkBuffer() {
        System.out.println((fin - ini) + ", " + buffer[0]);
    }
}
