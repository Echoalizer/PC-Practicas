package producersConsumers;

import java.util.concurrent.Semaphore;

public class ControllerSem implements ProducerConsumerController {

    private final Semaphore empty;
    private final Semaphore full = new Semaphore(0);
    private final Semaphore mutexC = new Semaphore(1);
    private final Semaphore mutexP = new Semaphore(1);

    public ControllerSem(int N) {
        this.empty = new Semaphore(N);
    }

    @Override
    public void acquireProd() throws InterruptedException {
        if (empty.hasQueuedThreads()) System.out.println("esperandoooo");
        empty.acquire();
        mutexP.acquire();
    }

    @Override
    public void releaseProd() {
        mutexP.release();
        full.release();
    }

    @Override
    public void acquireCons() throws InterruptedException {
        full.acquire();
        mutexC.acquire();
    }

    @Override
    public void releaseCons() {
        mutexC.release();
        empty.release();
    }
}
