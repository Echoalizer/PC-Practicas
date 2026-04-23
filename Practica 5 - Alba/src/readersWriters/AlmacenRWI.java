package readersWriters;


import java.io.Serializable;

public interface AlmacenRWI<T extends Serializable> {
    public boolean escribir(T producto, int pos);

    public T leer(int pos);
}
