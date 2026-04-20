package control;

import java.util.concurrent.Semaphore;

public class RWSem implements ReadWriteController {
    private final Semaphore r = new Semaphore(0);
    private final Semaphore w = new Semaphore(0);
    private final Semaphore e = new Semaphore(1);

    private int nr = 0;
    private int nw = 0;
    private int dr = 0;
    private int dw = 0;

    @Override
    public void request_read() throws InterruptedException {
        e.acquire();
        if (nw > 0) {
            dr++;
            e.release();
            r.acquire();
        }
        nr++;
        if (dr > 0) {
            dr--;
            r.release();
        } else e.release();
    }

    @Override
    public void request_write() throws InterruptedException {
        e.acquire();
        if (nw > 0 || nr > 0) {
            dw++;
            e.release();
            w.acquire();
        }
        nw++;
        e.release();
    }

    @Override
    public void release_read() throws InterruptedException {
        e.acquire();
        nr--;
        if (nr == 0 && dw > 0) {
            dw--;
            w.release();
        } else e.release();
    }

    @Override
    public void release_write() throws InterruptedException {
        e.acquire();
        nw--;
        if (dr > 0) {
            dr--;
            r.release();
        } else if (dw > 0) {
            dw--;
            w.release();
        } else e.release();
    }
}
