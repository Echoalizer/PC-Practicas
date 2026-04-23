package servidor;

import readersWriters.AlmacenRWI;
import readersWriters.LockRWMonitor;
import readersWriters.ReadWriteController;

import java.io.Serializable;
import java.util.Set;


public class ListaConcurrente<T extends Serializable> implements AlmacenRWI<T> {

    private final ReadWriteController controller;
    private final Set<T> valores;

    public ListaConcurrente(Set<T> valores) {
        controller = new LockRWMonitor();
        this.valores = valores;
    }

    @Override
    public boolean escribir(T valor, int pos) {
        boolean ok = false;
        try {
            controller.request_write();
            ok = valores.add(valor);
            controller.release_write();
        } catch (InterruptedException e) {
            // TODO catch block
        }

        return ok;
    }

    @Override
    public T leer(int pos) {
        T valor = null;
        try {
            controller.request_read();
//            valor = valores
            controller.release_read();
        } catch (InterruptedException e) {
            // TODO catch block
        }
        return valor;
    }
}
