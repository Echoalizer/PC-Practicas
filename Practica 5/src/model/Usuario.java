package model;

import java.net.SocketAddress;
import java.util.List;

public class Usuario {
    private final String id;
    private SocketAddress ipAddress;
    private List<Musica> musicas;

//    public Usuario(String id) {
//        this.id = id;
//    }

    public Usuario(String id, SocketAddress ipAddress) {
        this.id = id;
        this.ipAddress = ipAddress;
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

    public List<Musica> getSharedData() {
        return this.musicas;
    }
}
