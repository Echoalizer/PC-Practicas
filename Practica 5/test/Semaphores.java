import util.Entero;
import util.ThreadWithSemaphore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Semaphores {
    public static void main(String[] args) {
        int p = Integer.parseInt(args[0]);
        int n = Integer.parseInt(args[1]);

        // Sección crítica
        // creamos N threads que para sincronizarse usan un semáforo
        Entero k = new Entero(0);
        List<Thread> threads = new ArrayList<>();
        Semaphore sem = new Semaphore(1);

        // Creamos M procesos que incrementan y M que decrementan
        System.out.printf("Lanzando %d procesos de cada tipo...", p);
        for (int i = 0; i < p; i++) {
            var tdown = new ThreadWithSemaphore(2 * i, n, sem, k::decrementar);
            tdown.start();
            threads.add(tdown);

            var tup = new ThreadWithSemaphore((2 * i) + 1, n, sem, k::incrementar);
            tup.start();
            threads.add(tup);
        }

        for (var t : threads) {
            try {
                t.join();
            } catch (InterruptedException ignored) {
            }
        }
        System.out.println("\nAll threads terminated.");

        System.out.printf("Valor final de k: %d\n", k.get_valor());

    }
}
