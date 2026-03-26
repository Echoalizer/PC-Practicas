package util;

import java.util.concurrent.Semaphore;

public class ThreadWithSemaphore extends Thread {
    private final int id;
    private final int iterations;
    private final Runnable runnable;
    private final Semaphore sem;

    public ThreadWithSemaphore(int id, int iterations, Semaphore s, Runnable r) {
        this.id = id;
        this.setName(String.valueOf(id));
        this.iterations = iterations;
        this.sem = s;
        this.runnable = r;
    }

    @Override
    public void run() {
        for (int i = 0; i < iterations; i++) {
            try {
                sem.acquire();  // P(sem)
                runnable.run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                sem.release();  // V(sem) //! deberia estar en el try?
            }
        }
    }

}
