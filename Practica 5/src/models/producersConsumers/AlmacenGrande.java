package models.producersConsumers;

import control.Controller;
import control.ControllerSem;
import util.Producto;

public class AlmacenGrande implements Almacen {
    private final Controller controller;

    private final int N;
    private final Producto[] buffer;  // usado como buffer circular de tamaño N

    private int ini = 0, fin = 0;

    public AlmacenGrande(int N) {
        this.N = N;
        this.buffer = new Producto[N];
        this.controller = new ControllerSem(N);
    }

    @Override
    public void almacenar(Producto producto) {
        try {
            controller.acquireProd();

            buffer[fin] = producto;
            fin = (fin + 1) % N;

            controller.releaseProd();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Producto extraer() {
        Producto ret = null;
        try {
            controller.acquireCons();

            ret = buffer[ini];  // null si estuviera vacío
            buffer[ini] = null;
            ini = (ini + 1) % N;

            controller.releaseCons();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ret;
    }



    public void checkBuffer() {
        System.out.println((fin - ini) + ", " + buffer[0]);
    }
}
