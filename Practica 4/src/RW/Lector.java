package RW;

import util.Producto;

public class Lector extends Thread {
    private final int id;
    private final AlmacenRWI al;
    private final int N;
    private final int iteraciones;

    public Lector(int id, AlmacenRWI almacen, int N, int iteraciones) {
        this.id = id;
        this.al = almacen;
        this.N = N;
        this.iteraciones = iteraciones;
    }

    @Override
    public void run() {
        int pos = 0;
//      while (true) {
        for (int i = 0; i < iteraciones; i++) {
            Producto p = al.leer(pos);
            System.out.printf("recien leí el %s de la pos %d grupo soy %d.\n", p, pos, id);
            pos = (pos + 1) % N;
        }
    }
}
