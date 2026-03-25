package monitors;

import java.util.concurrent.locks.ReentrantLock;

public class MonitorLC implements MonitorIncDec {
    private int k = 0;
    private final ReentrantLock l = new ReentrantLock(true);

    public MonitorLC(int k) {
        this.k = k;
    }

    @Override
    public void increment() {
        l.lock();
        k++;
        l.unlock();
    }

    @Override
    public void decrement() {
        l.lock();
        k--;
        l.unlock();
    }
}
