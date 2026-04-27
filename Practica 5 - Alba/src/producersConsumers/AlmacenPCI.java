package producersConsumers;

public interface AlmacenPCI {

    /**
     * Almacena (como último) un producto en el almacén. Si no hay
     * hueco el proceso que ejecute el metodo bloqueará hasta que lo
     * haya.
     */
    public void enviar(String mensaje) throws InterruptedException;

    /**
     * Extrae el primer producto disponible. Si no hay productos el
     * proceso que ejecute el metodo bloqueará hasta que se almacene un
     * dato.
     */
    public String extraer() throws InterruptedException;
}
