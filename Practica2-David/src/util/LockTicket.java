package util;

import java.util.concurrent.atomic.AtomicInteger;

public class LockTicket implements CustomLock {
    private volatile int next;
    private final AtomicInteger number;
    private volatile Entero[] turns;

    public LockTicket(int p) {
        next = 1;
        number = new AtomicInteger(1);
        turns = new Entero[p];
        for (int i = 0; i < p; i++)
            turns[i] = new Entero();
    }

    @Override
    public boolean takeLock(int i) {
        turns[i].set_valor(number.getAndIncrement());
        while (next != turns[i].get_valor())
            ;
        return false;
    }

    @Override
    public void releaseLock(int i) {
        next++;
    }
}
