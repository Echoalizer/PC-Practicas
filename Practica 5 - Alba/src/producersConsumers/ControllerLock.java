package producersConsumers;

import locks.LockBakery;
import locks.LockId;

public class ControllerLock implements ProducerConsumerController {
    private final LockId prodLock;
    private final LockId consLock;

    int p, N;

    public ControllerLock(int N) {
        this.prodLock = new LockBakery(15);
        this.consLock = new LockBakery(15);
        this.N = N;  // máximo de elementos en el buffer
        this.p = 0;  // numero de elementos disponibles
    }

    @Override
    public void acquireProd(int id) {
        this.prodLock.takeLock(id);
        // Comprobamos si el buffer está lleno para no sobrepasarlo
        while (p == N)
            ;
        p++;
    }

    @Override
    public void acquireCons(int id) {
        this.consLock.takeLock(id);
        // comprobamos si hay algun puerto disponible, o sea, si el buffer no está vacío
        while (p == 0)
            ;
        p--;
    }

    @Override
    public void releaseProd(int id) {
        this.prodLock.releaseLock(id);
    }

    @Override
    public void releaseCons(int id) {
        this.consLock.releaseLock(id);
    }
}
