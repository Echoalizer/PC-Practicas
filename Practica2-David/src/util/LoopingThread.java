package util;

import java.util.function.Consumer;

public class LoopingThread extends Thread {
    private final int counter;
    private final Consumer<Integer> consumer;
    private String message;
    private Integer[] format;

    public LoopingThread(int iterations, Consumer<Integer> c) {
        this.counter = iterations;
        this.consumer = c;
    }

    public LoopingThread(int iterations, Consumer<Integer> c, String finalMessage, Integer... formattable) {
        this(iterations, c);
        this.message = finalMessage;
        this.format = formattable;
    }

    @Override
    public void run() {
        for (int i = 0; i < counter; i++)
            consumer.accept(i);

        if (message != null)
            System.out.printf(message, (Object[]) format);
    }

}
