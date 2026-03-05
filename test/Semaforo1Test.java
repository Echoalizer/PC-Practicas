import semaforos.Semaforo;
import util.Entero;
import util.ThreadWithSemaphore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import static util.Constants.ITERATIONS;
import static util.Constants.PROCESSES;

public class Semaforo1Test {

    public static void main(String[] args) {

        // Sección crítica
        // creamos N threads que para sincronizarse usan un semáforo
        Entero k = new Entero(0);
        List<Thread> threads = new ArrayList<>();
//        Semaforo sem = new Semaforo(1);
        Semaphore sem = new Semaphore(1);

        // Creamos M procesos que incrementan y M que decrementan
        System.out.printf("Lanzando %d procesos de cada tipo...", PROCESSES);
        for (int i = 0; i < PROCESSES; i++) {
            var tdown = new ThreadWithSemaphore(2 * i, ITERATIONS, sem, k::decrementar);
            tdown.start();
            threads.add(tdown);

            var tup = new ThreadWithSemaphore((2 * i) + 1, ITERATIONS, sem, k::incrementar);
            tup.start();
            threads.add(tup);
        }

        for (var t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("\nAll threads terminated.");

        System.out.printf("Valor final de k: %d\n", k.get_valor());

    }
}
