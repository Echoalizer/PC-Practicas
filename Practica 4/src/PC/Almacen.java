package PC;

import util.Producto;

public interface Almacen {

    /**
     * Almacena (como último) un producto en el almacén. Si no hay
     * hueco el proceso que ejecute el metodo bloqueará hasta que lo
     * haya.
     */
    public void almacenar(Producto producto) throws InterruptedException;

    /**
     * Extrae el primer producto disponible. Si no hay productos el
     * proceso que ejecute el metodo bloqueará hasta que se almacene un
     * dato.
     */
    public Producto extraer() throws InterruptedException;
}
