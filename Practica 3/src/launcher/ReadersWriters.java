package launcher;

import readersWriters.*;

import java.util.ArrayList;
import java.util.List;

public class ReadersWriters {

    public static void main(String[] args) {
        int TAM_BUFFER = Integer.parseInt(args[0]);
        int R = Integer.parseInt(args[1]);
        int W = Integer.parseInt(args[2]);
        int ITER = Integer.parseInt(args[3]);

        AlmacenRWI almacen = new AlmacenRW(TAM_BUFFER);

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < R; i++) {
            var lector = new Lector(i + 1, almacen, TAM_BUFFER, ITER);
            lector.start();
            threads.add(lector);
        }

        for (int i = 0; i < W; i++) {
            var escritor = new Escritor(i + 1, almacen, TAM_BUFFER, ITER);
            escritor.start();
            threads.add(escritor);
        }

        for (var t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("\nAll threads terminated.\n");
    }
}
