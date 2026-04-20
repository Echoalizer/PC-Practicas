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
        name = "oyente@" + s.getInetAddress().getHostAddress() + ":" + s.getPort();
        try {
            fin = new ObjectInputStream(s.getInputStream());
            // Object streams normally (but not always) auto-flush
            fout = new ObjectOutputStream(s.getOutputStream());
        } catch (IOException e) {
            System.out.printf("Read failed: %s", e.getMessage());
            System.exit(-1);
        }
    }

    @Override
    public void run() {
        try {
            listen: while (true) {  // label used to break loop
                Mensaje msg = (Mensaje) fin.readObject();
                System.out.printf("%s %s\n", name, msg);

                switch (msg.getTipo()) {
                    case "conexion":
                        fout.writeObject(new Mensaje("confirmacion_conexion"));
                        break;
                    case "pedir":
                        String str = (String) msg.getObject();
                        Entero k = new Entero(Integer.parseInt(str));
                        fout.writeObject(new Mensaje("devolver", k)); // del servidor
                        break;
                    case "desconexion":
                        fout.close();
                        fin.close();
                        s.close();
                        break listen;
                }
            }
        } catch (EOFException e) {
            System.out.printf("Client '%s' disconnected abruptly.\n", name);
        } catch (IOException | ClassNotFoundException e) {
            System.out.printf("Client '%s' ended with error: %s\n", name, e.getMessage());
        }
    }
}
