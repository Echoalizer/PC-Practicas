package app;

import model.Entero;
import model.Mensaje;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class OyenteServidor extends Thread {
    private final String hostName;

    private final Socket s;

    private final ObjectOutputStream fout;
    private final ObjectInputStream fin;

    OyenteServidor(Socket s) throws IOException {
        this.s = s;
        System.out.printf("open at %s:%d\n", s.getLocalAddress(), s.getLocalPort());
        System.out.printf("connected to %s:%d\n", s.getInetAddress(), s.getPort());
        hostName = "server@" + s.getInetAddress().getHostAddress();
        try {
            fout = new ObjectOutputStream(s.getOutputStream());
            fin = new ObjectInputStream(s.getInputStream());
        } catch (IOException e) {
            System.err.println("Could not create channel: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void run() {
        try {
            fout.writeObject(new Mensaje("conexion"));
            Mensaje confirmacion = (Mensaje) fin.readObject();

            System.out.println("Conexión establecida: " + confirmacion.getObject());

            Scanner reader = new Scanner(System.in);

            while (true) {
                System.out.printf("%s %% ", hostName);
                String[] msgs = reader.nextLine().split(" ");
                String msg = msgs[0], dato = null;

                if (msgs.length > 1)
                    dato = msgs[1];

                // Envío de mensajes
                Mensaje command = new Mensaje(msg, dato);

                fout.writeObject(command);

                if (command.getTipo().equals("desconexion")) {
                    reader.close();
                    fout.close();
                    fin.close();
                    s.close();
                    System.exit(0);
                }

                // Tratamiento de mensajes del servidor
                Mensaje answer = (Mensaje) fin.readObject();
                if (answer.getTipo().equals("devolver")) {
                    Entero e = (Entero) answer.getObject();
                    System.out.println(e.get_valor());
                }
            }

        } catch (Exception e) {
            System.err.printf("Server unreachable: %s\n", e.getMessage());
            System.exit(-1);
        }
    }
}
