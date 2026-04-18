package app;

import model.Entero;
import model.Mensaje;

import java.io.*;
import java.net.Socket;

public class OyenteCliente extends Thread {
    private final String name;
    private final Socket s;

    private ObjectOutputStream fout;
    private ObjectInputStream fin;

    OyenteCliente(Socket s) throws IOException {
        this.s = s;
        name = "oyente@" + s.getInetAddress().toString();
        try {
            fin = new ObjectInputStream(s.getInputStream());
            fout = new ObjectOutputStream(s.getOutputStream());  // flush?
        } catch (IOException e) {
            System.out.println("Read failed");
            System.exit(-1);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Mensaje msg = (Mensaje)fin.readObject();

                switch(msg.getTipo()) {
                    case "conexion":
                        fout.writeObject(new Mensaje("confirmacion_conexion"));
                        break;
                    case "pedir":
                        String str = (String)msg.getObject();
                        Entero k = new Entero (Integer.parseInt(str));
                        fout.writeObject(new Mensaje("devolver", k)); // del servidor
//                        fout.flush();
                        break;
                    case "desconexion":
                        fout.close();
                        fin.close();
                        s.close();
                        System.exit(0);
                }
                System.out.println(msg);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
