package app;

import model.Entero;
import model.Mensaje;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class OyenteServidor extends Thread {
    private final String name;
    private final Socket s;

    private ObjectOutputStream fout;
    private ObjectInputStream fin;

    OyenteServidor(Socket s) throws IOException {
        this.s = s;
        name = "server@" + s.getInetAddress().toString();
        try {
            fout = new ObjectOutputStream(s.getOutputStream());  // flush?
            fin = new ObjectInputStream(s.getInputStream());
        } catch (IOException e) {
            System.out.println("Read failed");
            System.exit(-1);
        }
    }

    @Override
    public void run() {
        try {
            System.out.printf("open at %s:%d\n", s.getLocalAddress(), s.getLocalPort());
            System.out.printf("connected to %s:%d\n", s.getInetAddress(), s.getPort());

            String host = s.getInetAddress().toString();


            fout.writeObject(new Mensaje("conexion"));
            Mensaje confirmacion = (Mensaje)fin.readObject();
            System.out.println(confirmacion.getTipo());

            Scanner reader = new Scanner(System.in); // Reading from System.in

            while (true) {
                System.out.printf("%s %% ", host);
                String msg = reader.nextLine();

                String dato = null;
                if (msg.equals("pedir"))
                    dato = reader.nextLine();

                Mensaje command = dato != null
                        ? new Mensaje(msg, dato)
                        : new Mensaje(msg);

                fout.writeObject(command);
                if (command.getTipo().equals("desconexion")) {
                    reader.close();
                    fout.close();
                    fin.close();
                    s.close();
                    System.exit(0);
                }

                Mensaje answer = (Mensaje)fin.readObject();
                switch(answer.getTipo()) {
                    case "devolver":
                        Entero e = (Entero)answer.getObject();
                        System.out.println(e.get_valor());
                        break;
                    default:
                        break;
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
