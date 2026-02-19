package util;

import java.util.concurrent.atomic.AtomicInteger;

public class LockBakery implements CustomLock {
    private int[] takes;

    public LockBakery(int p) {
        takes = new int[p];
    }

    @Override
    public boolean takeLock(int i) {
        return false;
    }

    @Override
    public void releaseLock(int i) {
        takes[i] = 0;
    }
}
