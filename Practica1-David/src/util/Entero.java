package util;

public class Entero {
    private volatile int n;

    public Entero() {
        n = 0;
    }

    public void incrementar() {
        n++;
    }

    public void decrementar() {
        n--;
    }

    public void print() {
        System.out.printf("%d", n);
    }

}
