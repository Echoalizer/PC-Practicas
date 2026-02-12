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
        int sub_program = Integer.parseInt(args[0]);

        int a = Integer.parseInt(args[1]);
        int b = args.length > 2 ? Integer.parseInt(args[2]) : MAX_TIME;

        List<Thread> threads = new ArrayList<>();

        if (sub_program == 1)
            parte1(threads, a, b);
        else if (sub_program == 2)
            parte2(threads, a, b);
        else if (sub_program == 3)
            parte3(threads, a, b);
        else {
            System.out.println("program argument not valid");
            System.exit(-1);
        }

        for (var t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (sub_program == 2) k.print();
        if (sub_program == 3) print_matrix(C, a);
        System.out.println("\nAll threads terminated.");
    }

    // Creación de procesos: Indeterminismo causado por la ejecución concurrente.
    private static void parte1(List<Thread> threads, int N, int T) {
        for (int i = 0; i < N; ++i) {
            String name = "Th" + i;

            int time = rand.nextInt(T);
            var t = new Thread(() -> {
                System.out.printf("Thread \"%s\" will sleep for %d ms\n", name, time);
                try {
                    Thread.sleep(time);
                    System.out.println(name);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, name);

            t.start();
            threads.add(t);
        }
    }

    // Provocar salida indeterminista: Modificación concurrente y sin guardas de un dato.
    private static void parte2(List<Thread> threads, int M, int N) {
        for (int i = 0; i < M; i++) {
            // decrementadores
            var tdown = new LoopingThread(N, (dummy) -> k.decrementar());
            // incrementadores
            var tup = new LoopingThread(N, (dummy) -> k.incrementar());

            tdown.start();
            threads.add(tdown);
            tup.start();
            threads.add(tup);
        }
    }

    // Multiplicación de matrices NxN, cada thread calculando una fila.
    private static void parte3(List<Thread> threads, int N, int max_range) {
        C = new int[N][N];  // static field
        int[][] A = new int[N][N], B = new int[N][N];

        // first we need to populate de matrices
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++) {
                A[i][j] = rand.nextInt(max_range);
                B[i][j] = rand.nextInt(max_range);
            }

        // we are using a lot of complexity to print the matrices
        print_matrix(A, N);
        print_matrix(B, N);

        for (int i = 0; i < N; i++) {
            int iLocal = i;
            var t = new LoopingThread(N, (j) -> {
                C[iLocal][j] = 0;
                for (int k = 0; k < N; k++)
                    C[iLocal][j] = C[iLocal][j] + A[iLocal][k] * B[k][j];
            }, "row %d calculated successfully\n", iLocal);
            t.start();
            threads.add(t);
        }
    }

    private static void print_matrix(int[][] m, int N) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++)
                System.out.printf("[%d] ", m[i][j]);
            System.out.println();
        }
        System.out.println();
    }
}