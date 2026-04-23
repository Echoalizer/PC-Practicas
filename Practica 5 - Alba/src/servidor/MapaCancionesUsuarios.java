package servidor;

import readersWriters.LockRWMonitor;
import readersWriters.ReadWriteController;
import utils.Usuario;

import java.io.IOException;
import java.util.HashMap;

///
/// Sigue la misma estructura que AlmacenRWI por ser también el modelo de escritores y lectores.
///
public class MapaCancionesUsuarios {

    private final ReadWriteController controller;

    // cambiar para permitir listas de usuarios.
    private HashMap<String, Usuario> valores;

    public MapaCancionesUsuarios() {
        controller = new LockRWMonitor();
        valores = new HashMap<>();
    }

    public boolean escribir(String key, Usuario user) throws InterruptedException, IOException {
        boolean insertado = false;
        controller.request_write();
        if (valores.containsKey(key)) {
            throw new IOException("Ya existe una cancion con ese id.");
        } else {
            // solo guardamos un usuario por cada canción
            valores.put(key, user);
            insertado = true;
        }
        controller.release_write();
        return insertado;
    }

    public boolean borrar(String key) throws InterruptedException, IOException {
        boolean borrado = false;
        controller.request_write();
        if (valores.containsKey(key)) {
            valores.remove(key);
            borrado = true;
        } else {
            throw new IOException("No existe una cancion con ese id.");
        }
        controller.release_write();
        return borrado;
    }

    public Usuario leer(String key) throws InterruptedException {
        Usuario user = null;
        controller.request_read();
        if (valores.containsKey(key))
            user = valores.get(key);
        controller.release_read();
        return user;
    }
}
