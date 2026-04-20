package app;

import model.Mensaje;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConexionCC extends Thread {
    private Socket socket;
    private ObjectInputStream cin;
    private ObjectOutputStream cout;
//    private int ttl;

    public ConexionCC() {

    }

    public ConexionCC(Socket socket) {

    }

    @Override
    public void run() {
        try {
            cout.writeObject(new Mensaje("conexion"));
        } catch (IOException e) {

        }
    }

}
