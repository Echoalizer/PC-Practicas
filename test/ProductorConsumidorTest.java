import models.AlmacenPeque;
import models.Consumidor;
import models.Productor;
import util.ThreadWithLock;

import java.util.ArrayList;
import java.util.List;

import static util.Constants.ITERATIONS;

public class ProductorConsumidorTest {

    public static void main(String[] args) {

        AlmacenPeque almacenPeque = new AlmacenPeque();

        List<Thread> threads = new ArrayList<>();

        var prod1 = new Productor(1, almacenPeque, 30);
        prod1.start();
        threads.add(prod1);
        var prod2 = new Productor(2, almacenPeque, 30);
        prod2.start();
        threads.add(prod2);

        var cons1 = new Consumidor(1, almacenPeque, 20);
        cons1.start();
        threads.add(cons1);
        var cons2 = new Consumidor(2, almacenPeque, 20);
        cons2.start();
        threads.add(cons2);
        var cons3 = new Consumidor(3, almacenPeque, 20);
        cons3.start();
        threads.add(cons3);


        for (var t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        almacenPeque.checkBuffer();
        System.out.println("\nAll threads terminated.\n");
    }
}
