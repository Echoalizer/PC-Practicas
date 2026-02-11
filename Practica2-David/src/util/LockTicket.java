package util;

import java.util.concurrent.atomic.AtomicInteger;

public class LockTicket extends CustomLock {
    private int next;
    private final AtomicInteger number;
    private final int[] takes;

    public LockTicket(int p) {
        super(p);
        next = 0;
        number = new AtomicInteger(0);
        takes = new int[p];
    }

    @Override
    public boolean takeLock(int i) {
        takes[i] = number.getAndIncrement();  // is this what we want?
        while (next != takes[i])
            ;
        return false;
    }

    @Override
    public void releaseLock(int ignored) {
        next++;
    }
}
