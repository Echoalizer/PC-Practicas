package utils;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

public class Usuario implements Serializable {
    private String username;
    private String ipAddress;
    private Set<Cancion> canciones;

    public Usuario(String username, String ip) {
        this.username = username;
        this.ipAddress = ip;

        this.canciones = new TreeSet<>();
    }

    public String getUsername() {
        return username;
    }

    public String getIp() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Set<Cancion> getCanciones() {
        return canciones;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != this.getClass())
            return false;
        return this.username.equals(((Usuario) obj).username);
    }

    @Override
    public String toString() {
        return String.format("%s - %s", this.username, this.ipAddress);
    }

    @Override
    public int hashCode() {
        return this.username.hashCode();
    }
}
