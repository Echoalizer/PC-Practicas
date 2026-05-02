package concurrent;

import cliente.Cliente;
import producersConsumers.SharedBuffer;

import java.io.PrintStream;

// La consola es un consumidor del modelo productor-consumidor
public class Consola extends Thread {
    private final SharedBuffer buffer;
    private final PrintStream out = System.out;
    private final PrintStream err = System.err;

    private boolean debug = true;

    public Consola(SharedBuffer buffer) {
        this.buffer = buffer;
    }

    public Consola(SharedBuffer buffer, boolean debug) {
        this(buffer);
        this.debug = debug;
    }

    @Override
    public void run() {
        // while (alive)
        while (Cliente.running) {
            try {
                String str = buffer.extraer();
                if (str.startsWith("DEBUG")) {
                    if (debug) out.print(str);
                } else if (str.startsWith("ERROR"))
                    err.print(str.substring(6));
                else out.print(str);
                // usar print + \n
            } catch (InterruptedException e) {
                err.println("Error de concurrencia en el controlador");
            }
        }
        if (debug) out.println("DEBUG Cerrando consola...");
    }

    public boolean setDebug(boolean debug) {
        this.debug = debug;
        return debug;
    }

}
