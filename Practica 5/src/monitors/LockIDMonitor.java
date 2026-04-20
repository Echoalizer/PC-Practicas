package monitors;

import java.util.concurrent.locks.ReentrantLock;

public class LockIDMonitor implements MonitorID {
    // Tiene sentido un controlador abstracto para este problema?
    private int k = 0;
    private final ReentrantLock l = new ReentrantLock(true);

    public LockIDMonitor(int k) {
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
    
    public int getValue() {
    	l.lock();
        int res = k;
        l.unlock();
    	return res;
    }
    
}
