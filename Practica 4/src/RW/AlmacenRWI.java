package RW;

import util.Producto;

public interface AlmacenRWI {
    public void escribir(Producto producto, int pos);
    public Producto leer(int pos);
}
