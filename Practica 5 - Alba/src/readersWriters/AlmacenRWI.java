package readersWriters;


import java.io.Serializable;
import java.util.List;

/**
 * Clase que representa un almacén para el modelo de lectores y escritores.
 * Métodos para añadir y borrar un elemento, y para obtener todos los de la lista.
 */
public interface AlmacenRWI<T extends Serializable> {

    public boolean escribir(T producto, String key) throws InterruptedException;

    public boolean borrar(T value) throws InterruptedException;

    public List<T> leerLista() throws InterruptedException;

}
