package util;

public abstract class CustomLock {
    protected final int processes;

    protected CustomLock(int p) {
        processes = p;
    }

    public abstract boolean takeLock(int i);
    public abstract void releaseLock(int i);
}
