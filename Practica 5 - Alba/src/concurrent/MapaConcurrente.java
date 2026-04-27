package concurrent;

import readersWriters.ReadWriteController;
import readersWriters.SyncRWMonitor;

import java.io.IOException;
import java.util.HashMap;


/// Sigue la misma estructura que AlmacenRWI por ser también el modelo de escritores y lectores.
public class MapaConcurrente<T> {

    private final ReadWriteController controller;

    private final HashMap<String, T> valores;

    public MapaConcurrente() {
        controller = new SyncRWMonitor();
        valores = new HashMap<>();
    }

    public boolean escribir(String key, T valor) throws InterruptedException, IOException {
        boolean insertado = false;
        controller.request_write();
        if (!valores.containsKey(key)) {
            // en el caso concreto, solo guardamos un usuario por cada canción
            valores.put(key, valor);
            insertado = true;
        }
        controller.release_write();
        return insertado;
    }

    public boolean borrar(String key) throws InterruptedException {
        boolean borrado = false;
        controller.request_write();
        if (valores.containsKey(key)) {
            valores.remove(key);
            borrado = true;
        }
        controller.release_write();
        return borrado;
    }

    public T leer(String key) throws InterruptedException {
        T valor = null;
        controller.request_read();
        if (valores.containsKey(key))
            valor = valores.get(key);
        controller.release_read();
        return valor;
    }
}
