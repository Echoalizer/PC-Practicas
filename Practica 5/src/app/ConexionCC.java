package app;

import model.Mensaje;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ConexionCC extends Thread {
    private Socket socket = null;
    private ObjectInputStream cin;
    private ObjectOutputStream cout;
//    private int ttl;

    public static Socket Listen() {
        Socket socket = null;
        try (ServerSocket ss = new ServerSocket(5000)) {
            socket = ss.accept();
        } catch (IOException e) {
            System.err.println("Could not connect.");
        }
        return socket;
    }

    public ConexionCC(Socket socket) {
        try {
            this.socket = socket;
            cin = new ObjectInputStream(socket.getInputStream());
            cout = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.err.println("Error reading socket data stream.");
        }
    }

    public ConexionCC(InetAddress address, int port) {
        try {
            socket = new Socket(address, port);
            cout = new ObjectOutputStream(socket.getOutputStream());
            cin = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.err.println("Could not connect to " + address + ":" + port);
        }
    }

    @Override
    public void run() {
        try {
            Mensaje msg = (Mensaje) cin.readObject();
            switch (msg.getTipo()) {
                case "conexion_cc":
                    break;
                case "confirmacion_conexion_cc":
                    break;
                case "solicitud_cancion_cc":
                    break;
                case "enviar_cancion_cc":
                    break;
                case "desconexion_cc":
                    break;
                default:
                    // mensaje desconocido
                    break;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Oh no!");
        }
    }

}
