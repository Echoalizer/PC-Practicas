package app;

import model.Usuario;

import java.io.ObjectOutputStream;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;

// concurrent
public class Canales {

    // mapa de clientes -> canales
    private final Map<SocketAddress, ObjectOutputStream> canales;

    public Canales() {
        canales = new HashMap<>();
    }

    public ObjectOutputStream get(Usuario u) {
        SocketAddress ip = u.getIpAddress();
        return canales.get(ip);
    }

    public void save(SocketAddress address, ObjectOutputStream out) {
        canales.put(address, out);
    }

//    public void save(InetAddress address, ObjectInputStream in) { }

}
