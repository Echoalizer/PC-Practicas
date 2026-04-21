package producersConsumers;

public interface ProducerConsumerController {
    void acquireProd() throws InterruptedException;

    void acquireCons() throws InterruptedException;

    void releaseProd();

    void releaseCons();
}
