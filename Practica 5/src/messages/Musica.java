package messages;

import java.io.Serializable;
import java.time.LocalDate;

public class Musica implements Serializable {
    private String titulo;
    private String artista;
    private boolean grupo;
    private LocalDate fecha;
    private int duracion;

    public Musica() {

    }

    public Musica(String titulo, String artista, boolean grupo, LocalDate fecha, int duracion) {
        this.titulo = titulo;
        this.artista = artista;
        this.grupo = grupo;
        this.fecha = fecha;
        this.duracion = duracion;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getArtista() {
        return artista;
    }

    public boolean isGrupo() {
        return grupo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public int getDuracion() {
        return duracion;
    }

    @Override
    public String toString() {
        return titulo + " by " + artista + " (" + fecha.getYear() + ")";
    }
}
