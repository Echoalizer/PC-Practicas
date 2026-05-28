package utils;

import java.io.Serializable;

public class Cancion implements Serializable {
    private final String id;
    private final String titulo;
    private final String artista;
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
        // 1. Verificación de identidad física. Se comprueba si ambos objetos apuntan al mismo (misma referencia) 
        if (this == o) return true;
        
        // 2. Verificación de nulidad y de clase
        if (o == null || getClass() != o.getClass()) return false;
        
        // 3. Vamos a comparar ahora realmente por aquel campo que realmente nos interesa, que es el id
        Cancion cancion = (Cancion) o;
        return java.util.Objects.equals(id, cancion.id);
    }

    @Override
    public int hashCode() {
        // El hash debe generarse usando los mismos campos que en el equals
        return java.util.Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("%s: '%s' by '%s'", this.id, this.titulo, this.artista);
    }

  
}
