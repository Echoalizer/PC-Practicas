package util;

public class LockBakery extends CustomLock {
    public LockBakery(int p) {
        super(p);
    }

    @Override
    public boolean takeLock(int i) {
        return false;
    }

    @Override
    public void releaseLock(int i) {

    }
}
