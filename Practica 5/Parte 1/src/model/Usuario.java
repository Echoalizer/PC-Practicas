package model;

import java.net.InetAddress;
import java.util.List;

public class Usuario {
    private final String id;
    private InetAddress ipAddress;
    private List<Musica> musicas;

    public Usuario(String id) {
        this.id = id;
    }

    public List<Musica> getSharedData() {
        return this.musicas;
    }
}
