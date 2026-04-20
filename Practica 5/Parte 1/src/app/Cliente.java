package app;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Cliente {

    public static void main (String[] args) {
        String IP = args[0];
        int port = Integer.parseInt(args[1]);

        try {
            Socket s = new Socket(IP, port);
            new OyenteServidor(s).start();

        } catch (UnknownHostException | ConnectException e) {
            System.err.printf("Host '%s' not found or unreachable: %s\n", IP, e.getMessage());
            System.exit(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
