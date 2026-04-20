package app;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class Servidor {
    // El servidor contiene los datos.json sobre qué información hay disponible en
    // el sistema y quiénes son los clientes propietarios de la información.
    private static final int DEFAULT_PORT = 9000;

    private static final String dir = "server_files/";

    public static void main (String[] args) {
        Almacen almacen = new Almacen(new SyncRWMonitor());
        Canales canales = new Canales();

        int puerto = DEFAULT_PORT;
        if  (args.length > 0)
            puerto = Integer.parseInt(args[0]);

        try (ServerSocket listen = new ServerSocket(puerto)) {
            System.out.printf("server reachable at %s:%d\n", InetAddress.getLocalHost().getHostAddress(), listen.getLocalPort());
            while (true) {
                Socket ss = listen.accept();
                (new OyenteCliente(ss, almacen, canales)).start();

            }
            // apagar y broadcast desconexion
        } catch (IOException e) {
            System.err.printf("error accepting %s\n", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
