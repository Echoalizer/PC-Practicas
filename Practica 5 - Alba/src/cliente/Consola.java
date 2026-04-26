package cliente;

import producersConsumers.SharedBuffer;

// La consola es un consumidor del modelo productor-consumidor
public class Consola extends Thread {

    private final SharedBuffer buffer;

    public Consola(SharedBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String str = buffer.extraer();
                // controlar si es out o err
                System.out.println(str);
            } catch (InterruptedException e) {
                System.err.println("Error de concurrencia en el controlador");
            }
        }
    }

}
