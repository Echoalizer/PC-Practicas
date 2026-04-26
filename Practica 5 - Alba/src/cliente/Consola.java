package cliente;

import producersConsumers.SharedBuffer;

import java.io.PrintStream;

// La consola es un consumidor del modelo productor-consumidor
public class Consola extends Thread {
    private final SharedBuffer buffer;
    private final PrintStream out = System.out;
    private final PrintStream err = System.err;

    private boolean debug = false;

    public Consola(SharedBuffer buffer) {
        this.buffer = buffer;
    }

    public Consola(SharedBuffer buffer, boolean debug) {
        this(buffer);
        this.debug = debug;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String str = buffer.extraer();
                if (str.startsWith("DEBUG")) {
                    if (debug) out.println(str.substring(5));
                } else if (str.startsWith("ERROR"))
                    err.println(str.substring(6));
                else out.println(str);
            } catch (InterruptedException e) {
                System.err.println("Error de concurrencia en el controlador");
            }
        }
    }

}
