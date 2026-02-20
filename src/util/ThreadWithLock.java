package util;

import locks.LockId;

public class ThreadWithLock extends Thread {
    private final int id;
    private final int iterations;
    private final Runnable runnable;
    private final LockId lock;

    public ThreadWithLock(int id, int iterations, LockId l, Runnable r) {
        this.id = id;
        this.setName(String.valueOf(id));
        this.iterations = iterations;
        this.lock = l;
        this.runnable = r;
    }

    @Override
    public void run() {
        for (int i = 0; i < iterations; i++) {
            lock.takeLock(id);
            runnable.run();
            lock.releaseLock(id);
        }
    }

}
