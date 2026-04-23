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
        empty.acquire();
//            if (empty.hasQueuedThreads()) System.out.println("wAITing...");
//            System.out.println(fin - ini);
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
//            if (full.hasQueuedThreads()) System.out.println("waiting...");
//            System.out.println(fin - ini);
        mutexC.acquire();
    }

    @Override
    public void releaseCons() {
        mutexC.release();
        empty.release();  // si hay error perdemos el dato
    }
}
