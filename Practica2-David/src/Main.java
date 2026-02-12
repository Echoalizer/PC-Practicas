import util.Entero;
import util.LoopingThread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    private static final Random rand = new Random();
    private static final int MAX_TIME = 4000;  // para la parte 1
    private static final Entero k = new Entero();  // a modificar por los distintos threads en la parte 2.
    private static int[][] C;

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);

        List<Thread> threads = new ArrayList<>();

        parte1(threads, n);

        for (var t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("\nAll threads terminated.");
        k.print();
    }

    // Evitar condición de carrera con espera activa.
    private static void parte1(List<Thread> threads, int N) {
        // Usando el algoritmo rompe-empates para dos procesos
        var tdown = new LoopingThread(N, (dummy) -> k.decrementar());
        tdown.start();
        threads.add(tdown);

        var tup = new LoopingThread(N, (dummy) -> k.incrementar());
        tup.start();
        threads.add(tup);
    }
}