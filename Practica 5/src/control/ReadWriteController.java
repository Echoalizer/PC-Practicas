package control;

public interface ReadWriteController {
    void request_read() throws InterruptedException;

    void request_write() throws InterruptedException;

    void release_read() throws InterruptedException;

    void release_write() throws InterruptedException;
}
