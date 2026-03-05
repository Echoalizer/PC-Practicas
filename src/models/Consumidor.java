package models;

import util.Producto;

public class Consumidor extends Thread {
    private final int id;
    private final Almacen al;
    private final int iteraciones;

    public Consumidor(int id, Almacen almacen, int iteraciones) {
        this.id = id;
        this.al = almacen;
        this.iteraciones = iteraciones;
    }

    @Override
    public void run() {
        for (int i = 0; i < iteraciones; i++) {
            Producto p = al.extraer();
            consumir(p);
        }
    }

    private void consumir(Producto p) {
        System.out.printf("consumidor %d consume: %s\n", id, p);
    }
}
