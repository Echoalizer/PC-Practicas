package concurrent;

import locks.LockBakery;
import locks.LockId;

public class LockedString {
    private String s;
    private final LockId lock;

    public LockedString(String s) {
        this.s = s;
        lock = new LockBakery(15);  // N es el número de procesos que pueden entrar a esperar
    }

    public String get(int id) {
        lock.takeLock(id);
        return s;
    }

    public void returnLock(int id) {
        lock.releaseLock(id);
    }
}
