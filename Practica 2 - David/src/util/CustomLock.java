package util;

public interface CustomLock {
    boolean takeLock(int i);
    void releaseLock(int i);
}
