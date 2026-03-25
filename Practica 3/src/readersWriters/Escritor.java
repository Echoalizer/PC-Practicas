package readersWriters;

import util.Producto;

public class Escritor extends Thread {
    private final int id;
    private final AlmacenRWI al;
    private final int N;
    private final int iteraciones;

    public Escritor(int id, AlmacenRWI almacen, int N, int iteraciones) {
        this.id = id;
        this.al = almacen;
        this.N = N;
        this.iteraciones = iteraciones;
    }

    @Override
    public void run() {
        int pos = 0;
        for (int i = 0; i < iteraciones; i++) {
            Producto p = new Producto(id, i);
            System.out.printf("voy a escribir el %s en %d.\n", p, pos);
            al.escribir(p, pos);
            pos = (pos + 1) % N;
        }
    }
}
