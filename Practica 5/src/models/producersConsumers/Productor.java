package models.producersConsumers;

import util.Producto;

public class Productor extends Thread {
    private final int id;
    private final Almacen al;
    private final int iteraciones;

    public Productor(int id, Almacen almacen, int iteraciones) {
        this.id = id;
        this.al = almacen;
        this.iteraciones = iteraciones;
    }

    @Override
    public void run() {
        for (int i = 0; i < iteraciones; i++) {
            Producto p = new Producto(id, i);
            try {
                al.almacenar(p);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
