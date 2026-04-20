package model;

import java.io.Serializable;
import java.time.LocalDate;

public class Musica implements Serializable {
    private String nombre;
    private String artista;
    private boolean grupo;
    private LocalDate fecha;
    private int duracion;

    public Musica() {

    }

    public Musica(String nombre, String artista,  boolean grupo, LocalDate fecha, int duracion) {
        this.nombre = nombre;
        this.artista = artista;
        this.grupo = grupo;
        this.fecha = fecha;
        this.duracion = duracion;
    }

    @Override
    public String toString() {
        return nombre + " by " + artista + " (" + fecha.getYear() + ")";
    }
}
