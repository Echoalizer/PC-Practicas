package models.readersWriters;

import control.RW;
import control.RWSem;
import util.Producto;

public class AlmacenRW implements AlmacenRWI {
    private final RW controller;

    private final Producto[] buffer;  // usado como buffer circular de tamaño N

    public AlmacenRW(int N) {
        this.buffer = new Producto[N];
        this.controller = new RWSem();
    }

    @Override
    public void escribir(Producto producto, int pos) {
        try {
            controller.request_write();

            buffer[pos] = producto;

            controller.release_write();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Producto leer(int pos) {
        Producto sol = null;
        try {
            controller.request_read();

            // leer
            sol = buffer[pos];
//            System.out.println(sol);

            controller.release_read();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return sol;
    }

    public void checkBuffer() {
        System.out.println( buffer[0]);
    }
}
