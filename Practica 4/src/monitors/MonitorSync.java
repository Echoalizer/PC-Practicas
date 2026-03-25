package monitors;

public class Monitor {
    private int k = 0;

    public Monitor(int k) {
        this.k = k;
    }

    public synchronized void incrementar() {
        this.k++;
    }

    public synchronized void decrementar() {
        this.k--;
    }
}
