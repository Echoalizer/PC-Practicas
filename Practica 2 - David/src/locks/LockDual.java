package locks;

import util.CustomLock;

public class LockDual implements CustomLock {
    private volatile boolean in[];
    private volatile int last = 0;

    public LockDual() {
        in = new boolean[2];
    }

    @Override
    public boolean takeLock(int i) {
        // PROTOCOLO DE ENTRY
        in[i] = true;
        last = i;
        while (in[(i + 1) % 2] && last == i)
            ; // Bucle de espera activa

        return false;
    }

    @Override
    public void releaseLock(int i) {
        // EXIT
        in[i] = false;
    }
}
