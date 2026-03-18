package producersConsumers;

import util.Producto;

import java.util.concurrent.Semaphore;

public class AlmacenPeque implements Almacen {
    private final Producto[] buffer = new Producto[1];
    private final Semaphore empty = new Semaphore(1);
    private final Semaphore full = new Semaphore(0);

    @Override
    public void almacenar(Producto producto) {
        try {
            empty.acquire();
            buffer[0] = producto;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            full.release();
        }
    }

    @Override
    public Producto extraer() {
        Producto ret = null;
        try {
            full.acquire();
            ret = buffer[0];  // null si estuviera vacío
            buffer[0] = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
//            System.out.println(ret);
            empty.release();
        }
        return ret;
    }

    public void checkBuffer() {
        System.out.println(buffer[0]);
    }
}
