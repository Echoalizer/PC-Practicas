package app;

public interface RW {
    void request_read() throws InterruptedException;

    void request_write() throws InterruptedException;

    void release_read();

    void release_write();
}
