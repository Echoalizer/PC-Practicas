package monitors;

public class SyncIDMonitor implements MonitorID {
    private int k = 0;

    public SyncIDMonitor(int k) {
        this.k = k;
    }

    @Override
    public synchronized void increment() {
        this.k++;
    }

    @Override
    public synchronized void decrement() {
        this.k--;
    }
    
    public synchronized int getValue() {
    	return this.k;
    }
    
}
