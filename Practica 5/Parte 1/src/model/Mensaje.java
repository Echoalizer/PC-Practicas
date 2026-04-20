package model;

import java.io.Serializable;

//public static final String CONEXION = "conexion";

public class Mensaje implements Serializable {
    private final String tipo;
    private Object o;

    public Mensaje(String tipo) {
        this.tipo = tipo;
    }

    public Mensaje(String tipo, Object o) {
        this(tipo);
        this.o = o;
    }

    public String getTipo() {
        return tipo;
    }

    public Object getObject() {
        return o;
    }

    @Override
    public String toString() {
        return tipo + (o != null ? ": " + o : "");
    }
}
