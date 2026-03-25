package util;

public class Producto {
    private final String s;

    public Producto(int p, int k) {
        this.s = String.valueOf(p) + "p" + String.valueOf(k);
    }

    @Override
    public String toString() {
        return s;
    }
}
