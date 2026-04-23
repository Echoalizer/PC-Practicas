package utils;

import java.io.Serializable;
import java.util.Set;

public class Usuario implements Serializable {
    String username;
    String ip;
    Set<Musica> canciones;

    public Usuario(String username, String ip) {
        this.username = username;
        this.ip = ip;

        this.canciones = new TreeSet<>();
    }


}
