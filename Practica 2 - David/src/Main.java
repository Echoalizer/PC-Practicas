import locks.LockDual;
import locks.LockTicket;
import util.*;

import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final Entero k = new Entero();

    public static void main(String[] args) {
        int p = Integer.parseInt(args[0]);
        int n = Integer.parseInt(args[1]);

        List<Thread> threads = new ArrayList<>();

        if (p == 1)
            parte1(threads, n);
        else {
            int m = Integer.parseInt(args[2]);
            parte2(threads, m, n);
        }

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
        var lock = new LockDual();
        var tdown = new LoopingThread(0, N, (dummy) -> k.decrementar(), lock);
        tdown.start();
        threads.add(tdown);

        var tup = new LoopingThread(1, N, (dummy) -> k.incrementar(), lock);
        tup.start();
        threads.add(tup);
    }

    private static void parte2(List<Thread> threads, int M, int N) {
        var lock = new LockTicket(2*M);
        for (int i = 0; i < M; i++) {
            var tdown = new LoopingThread(2*i, N, (dummy) -> k.decrementar(), lock);
            tdown.start();
            threads.add(tdown);

            var tup = new LoopingThread((2*i)+1, N, (dummy) -> k.incrementar(), lock);
            tup.start();
            threads.add(tup);
        }

    }
}