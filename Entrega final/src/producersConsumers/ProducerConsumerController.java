package producersConsumers;

public interface ProducerConsumerController {
    void acquireProd(int id) throws InterruptedException;

    void acquireCons(int id) throws InterruptedException;

    void releaseProd(int id);

    void releaseCons(int id);
}
