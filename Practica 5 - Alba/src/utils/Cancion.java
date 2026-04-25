package utils;

import java.io.Serializable;

public class Cancion implements Serializable {
    private final String id;
    private String titulo;
    private String artista;
    // esGrupo, fecha, duración

    public Cancion(String id, String titulo, String artista) {
        this.id = id; // auto-generado
        this.titulo = titulo;
        this.artista = artista;
    }

    public String getId() {
        return this.id;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public String getArtista() {
        return this.artista;
    }

    @Override
    public boolean equals(Object o) {
        // revisar
        if (o.getClass() != this.getClass())
            return false;
        else
            return this.id.equals(((Cancion) o).id);
    }

    @Override
    public String toString() {
        return String.format("Canción::%s{%s by %s}", this.id, this.titulo, this.artista);
    }
}
