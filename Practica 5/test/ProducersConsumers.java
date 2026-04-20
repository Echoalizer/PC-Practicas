import models.producersConsumers.AlmacenGrande;
import models.producersConsumers.AlmacenPeque;
import models.producersConsumers.Consumidor;
import models.producersConsumers.Productor;

import java.util.ArrayList;
import java.util.List;

public class ProducersConsumers {
    public static final int P = 6;
    public static final int C = 4;
    public static final int IT_PROD = 64;
    public static final int IT_CONS = 96;
    public static final int TAM_BUFFER = 10;

    public static void main(String[] args) {

        AlmacenPeque almacenPeque = new AlmacenPeque();
        AlmacenGrande almacenGrande = new AlmacenGrande(TAM_BUFFER);

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < P; i++) {
            var prod = new Productor(i + 1, almacenGrande, IT_PROD);
            prod.start();
            threads.add(prod);
        }

        for (int i = 0; i < C; i++) {
            var cons = new Consumidor(i + 1, almacenGrande, IT_CONS);
            cons.start();
            threads.add(cons);
        }

        for (var t : threads) {
            try {
                t.join();
            } catch (InterruptedException ignored) {
            }
        }

        almacenGrande.checkBuffer();
        System.out.println("\nAll threads terminated.\n");
    }
}
