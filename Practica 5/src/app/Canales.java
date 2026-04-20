package app;

import model.Usuario;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.util.Map;

// concurrent
public class Canales {
    // mapa de clientes -> canales
    private Map<InetAddress, ObjectOutputStream> clientes;

    // mapa de canales -> puertos
    private Map<Integer, ?> canales;

    public Canales() {}

    public void get(Usuario u) {

    }

    public void save(InetAddress address, ObjectOutputStream out) {

    }

    public void save(InetAddress address, ObjectInputStream in) {

    }

}
