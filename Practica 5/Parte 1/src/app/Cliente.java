package app;

import java.io.*;
import java.net.Socket;

public class Cliente {

    public static void main (String[] args) {
        String IP = args[0];
        int port = Integer.parseInt(args[1]);

        try {
            Socket s = new Socket(IP, port);
            new OyenteServidor(s).start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
