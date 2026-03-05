package semaforos;

import java.util.concurrent.atomic.AtomicInteger;

public class Semaforo {
    private final AtomicInteger mutex;

    public Semaforo() {
        mutex = new AtomicInteger();
        mutex.set(1);
    }

    public Semaforo(int value) {
        mutex = new AtomicInteger();
        mutex.set(value);
    }

    public void V() {
        mutex.getAndIncrement();
    }

    // esta implementación no es justa porque no guarda una cola de procesos que quieren entrar
    public void P() {
        while (mutex.compareAndExchange(1, 0) == 0)
            ;  // es una alternativa a espera activa, pero usa espera activa?
    }
}
