package util;

public class Entero {
    private volatile int n;
    private boolean in1 = false;
    private boolean in2 = true;
    private int last = 0;

    public Entero() {
        n = 0;
    }

    public Entero(int n) {
        this.n = n;
    }

    public void incrementar() {

        // PROTOCOLO DE ENTRY
        in1 = true;
        last = 1;
        while (in2 && last == 1)
            ; // Bucle de espera activa

        // Sección crítica
        n++;

        // PROTOCOLO EXIT
        in1 = false;
    }

    public void decrementar() {
        in2 = true;
        last = 2;
        while (in1 && last == 2)
            ;
        n--;
        in2 = false;
    }

    public void print() {
        System.out.printf("Value of n is: %d", n);
    }

}
