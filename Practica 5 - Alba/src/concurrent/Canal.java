package concurrent;

import locks.LockId;
import locks.LockTicket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Canal {
    private final ObjectOutputStream fout;
    private final ObjectInputStream fin;

    private final LockId lock;

    // dos constructores para evitar bloqueos si utilizamos siempre el mismo orden de creación.
    public Canal(ObjectOutputStream fout, ObjectInputStream fin) {
        this.fout = fout;
        this.fin = fin;
        this.lock = new LockTicket();
    }

    public Canal(ObjectInputStream fin, ObjectOutputStream fout) {
        this.fin = fin;
        this.fout = fout;
        this.lock = new LockTicket();
    }

    public void write(Object obj) throws IOException {
        // lock ticket no toma un valor de 'id' pero implementar la interfaz obliga a mantenerlo.
        this.lock.takeLock(0);
        this.fout.writeObject(obj);
        this.lock.releaseLock(0);
    }

    // no hace falta, implementado para preservar la encapsulación
    public Object read() throws IOException, ClassNotFoundException {
        // las restricciones de diseño no permiten acceder a más de un thread así que no hace falta tomar el lock.
        return this.fin.readObject();
    }

    public void close() throws IOException {
        this.fout.close();
        this.fin.close();
    }

}
