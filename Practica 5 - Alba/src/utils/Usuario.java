package utils;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

public class Usuario implements Serializable {
    String username;
    String ip;
    Set<Cancion> canciones;

    public Usuario(String username, String ip) {
        this.username = username;
        this.ip = ip;

        this.canciones = new TreeSet<>();
    }

    @Override
    public int hashCode() {

        return 0;
    }


    @Override
    public boolean equals(Object obj) {
        return this.username.equals(((Usuario) obj).username);
    }
}
