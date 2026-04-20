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

    public Usuario(String id, InetAddress ipAddress) {
        this.id = id;
        this.ipAddress = ipAddress;
    }

    public String getId() {
        return id;
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(InetAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    public List<Musica> getSharedData() {
        return this.musicas;
    }
}
