package app;

import model.Usuario;

import java.nio.channels.SocketChannel;
import java.util.Map;

// concurrent
public class Canales {
    // mapa de clientes -> canales
    //

    // mapa de canales -> puertos
    private Map<Integer, SocketChannel> canales;

    public Canales() {}

    public void get(Usuario u) {

    }

}
