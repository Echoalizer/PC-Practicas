package util;

import java.util.concurrent.atomic.AtomicInteger;

public class LockTicket implements CustomLock {
    private volatile int next;
    private AtomicInteger number;
    private volatile int[] turns;

    public LockTicket(int p) {
        next = 1;
        number = new AtomicInteger(1);
        turns = new int[p];
    }

    @Override
    public boolean takeLock(int i) {
        turns[i] = number.getAndIncrement();  // is this what we want?
        while (next != turns[i])
            ;
        return false;
    }

    @Override
    public void releaseLock(int i) {
        next++;
    }
}
