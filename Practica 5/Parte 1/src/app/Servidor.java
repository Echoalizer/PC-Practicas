package app;

import model.Musica;
import model.Usuario;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Servidor {
    // El servidor contiene los datos.json sobre qué información hay disponible en
    // el sistema y quiénes son los clientes propietarios de la información.
    private static final int DEFAULT_PORT = 9000;

    private static final String dir = "server_files/";

    Set<Usuario> usuarios;
    // Informacion (musica) que tiene cada usuario
    Map<Musica, Usuario> owners = new HashMap<Musica, Usuario>();
    // dentro de un monitor?

    // mapa de clientes -> canales
    //

    // mapa de canales -> puertos
    Map<Integer, SocketChannel> canales;

    public Servidor() {
        usuarios = new HashSet<Usuario>();
        owners = new HashMap<Musica, Usuario>();
        try {
            // load users from json file
            var file = new FileInputStream(dir + "datos.json");
        } catch (FileNotFoundException e) {

        }

        for (var user: usuarios) {
            for (var dato : user.getSharedData()) {
                owners.put(dato, user);
            }
        }

    }

    public static void main (String[] args) {
        int puerto = DEFAULT_PORT;
        if  (args.length > 0)
            puerto = Integer.parseInt(args[0]);

        try (ServerSocket listen = new ServerSocket(puerto)) {
            System.out.printf("server reachable at %s:%d\n", InetAddress.getLocalHost().getHostAddress(), listen.getLocalPort());
            while (true) {
                Socket ss = listen.accept();
                (new OyenteCliente(ss)).start();

            }
        } catch (IOException e) {
            System.out.printf("error accepting %s\n", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
