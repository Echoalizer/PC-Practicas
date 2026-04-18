package app;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    public static void main (String[] args) {
        try {
            ServerSocket listen = new ServerSocket(99);
            System.out.printf("server reachable at %s:%d\n", listen.getInetAddress(), listen.getLocalPort());
            while (true) {
                Socket ss = listen.accept();
                (new OyenteCliente(ss)).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
