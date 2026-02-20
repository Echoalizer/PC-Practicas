package util;

public class Entero {
    private volatile int n;

    public Entero() {
        n = 0;
    }

    public Entero(int n) {
        this.n = n;
    }

    public void set_valor(int num) {
        n = num;
    }

    public int get_valor() {
        return n;
    }

    public void incrementar() {
        n++;
    }

    public void decrementar() {
        n--;
    }

    public void print() {
        System.out.printf("Value of n is: %d", n);
    }
}
