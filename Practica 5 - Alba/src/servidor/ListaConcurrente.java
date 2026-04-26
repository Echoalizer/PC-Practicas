package servidor;

import readersWriters.AlmacenRWI;
import readersWriters.LockRWMonitor;
import readersWriters.ReadWriteController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class ListaConcurrente<T extends Serializable> implements AlmacenRWI<T> {

    private final ReadWriteController controller;
    private final Set<T> valores;

    public ListaConcurrente() {
        controller = new LockRWMonitor();
        valores = new HashSet<>();
    }

    @Override
    public boolean escribir(T value) throws InterruptedException {
        boolean insertado = false;
        try {
            controller.request_write();
            insertado = valores.add(value);
            controller.release_write();
        } catch (InterruptedException e) {
            throw new InterruptedException("No se ha podido escribir el valor.");
        }

        return insertado;
    }

    @Override
    public boolean borrar(T value) throws InterruptedException {
        boolean borrado = false;
        try {
            controller.request_write();
            borrado = valores.remove(value);
            controller.release_write();
        } catch (InterruptedException e) {
            throw new InterruptedException("No se ha podido borrar el valor.");
        }

        return borrado;
    }

    @Override
    public ArrayList<T> leerLista() throws InterruptedException {
        ArrayList<T> lista;
        try {
            controller.request_read();
            lista = new ArrayList<>(valores);
            controller.release_read();
        } catch (InterruptedException e) {
            throw new InterruptedException("No se ha podido leer el valor.");
        }

        return lista;
    }
}
