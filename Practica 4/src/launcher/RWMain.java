package launcher;

import java.util.ArrayList;
import java.util.List;

import RW.*;

public class RWMain {

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);  // tamaño del buffer
        int e = Integer.parseInt(args[1]);  // num prods; cons
        int l = Integer.parseInt(args[2]);
        int it = Integer.parseInt(args[3]);  // num iteraciones


        List<Thread> lista = new ArrayList<>();

        RW control = new LockRWMonitor();
        AlmacenRWI al = new AlmacenRW(n, control);

        for (int i = 0; i < e; i++) {
            var escritor = new Escritor(i + 1, al, n, it);
            lista.add(escritor);
            escritor.start();
        }

        for (int i = 0; i < l; i++) {
            var lector = new Lector(i + 1, al, n, it);
            lista.add(lector);
            lector.start();
        }


        for(var i : lista) {
            try {
                i.join();
            } catch (InterruptedException ignored) {
                ignored.printStackTrace();
            }
        }

        System.out.println();  // comprobacion
    }

}
