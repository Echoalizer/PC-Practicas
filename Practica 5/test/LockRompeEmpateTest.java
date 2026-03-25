import locks.LockRompeEmpate;
import util.*;

import java.util.ArrayList;
import java.util.List;

import static util.Constants.*;

class LockRompeEmpateTest {

    public static void main(String[] args) {

        Entero k = new Entero(0);
        List<Thread> threads = new ArrayList<>();
        LockRompeEmpate lock = new LockRompeEmpate(2 * PROCESSES);

        // Creamos M procesos que incrementan y M que decrementan
        System.out.printf("Lanzando %d procesos de cada tipo...", PROCESSES);
        for (int i = 0; i < PROCESSES; i++) {
            var tdown = new ThreadWithLock(2 * i, ITERATIONS, lock, k::decrementar);
            tdown.start();
            threads.add(tdown);

            var tup = new ThreadWithLock((2 * i) + 1, ITERATIONS, lock, k::incrementar);
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
