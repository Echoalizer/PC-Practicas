package util;

import java.util.function.Consumer;

public class LoopingThread extends Thread {
    private final int counter;
    private final Consumer<Integer> consumer;
    private final CustomLock lock;
    private final int id;
    private String message;
    private Integer[] format;

    public LoopingThread(int id, int iterations, Consumer<Integer> c, CustomLock l) {
        this.id = id;
        this.counter = iterations;
        this.consumer = c;
        this.lock = l;
    }

    public LoopingThread(int id, int iterations, Consumer<Integer> c, CustomLock l, String finalMessage, Integer... formattable) {
        this(id, iterations, c, l);
        this.message = finalMessage;
        this.format = formattable;
    }

    @Override
    public void run() {
        for (int i = 0; i < counter; i++) {
            lock.takeLock(id);
            consumer.accept(i);
            lock.releaseLock(id);
        }

        if (message != null)
            System.out.printf(message, (Object[]) format);
    }

}
