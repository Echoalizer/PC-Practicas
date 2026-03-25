package monitors;

public class MonitorSync implements MonitorIncDec {
    private int k = 0;

    public MonitorSync(int k) {
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
