import producersConsumers.AlmacenGrande;
import producersConsumers.AlmacenPeque;
import producersConsumers.Consumidor;
import producersConsumers.Productor;

import java.util.ArrayList;
import java.util.List;

import static util.Constants.*;

public class ProductorConsumidorTest {

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
