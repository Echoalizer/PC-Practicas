package model;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private final String id;
    private final List<Musica> musicas;

    private SocketAddress ipAddress;

    public Usuario(String id, SocketAddress ipAddress) {
        this.id = id;
        this.ipAddress = ipAddress;
        this.musicas = new ArrayList<>();


    }

    public String getId() {
        return id;
    }

    public SocketAddress getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(SocketAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void removeIpAddress() {
        this.ipAddress = null;
    }

    public List<Musica> getSharedData() {
        return this.musicas;
    }
}
