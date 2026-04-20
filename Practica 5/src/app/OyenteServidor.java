package app;

import model.Mensaje;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketAddress;
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
            Scanner reader = new Scanner(System.in);

            System.out.println("login: ");
            String login = reader.nextLine();

            // Mensaje de conexión
            fout.writeObject(new Mensaje("conexion_cs", login));  // login aquí
            Mensaje confirmacion = (Mensaje) fin.readObject();

            System.out.println("Conexión establecida: " + confirmacion.getObject());

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
                Mensaje answer = (Mensaje)fin.readObject();
                switch(answer.getTipo()) {
                    case "confirmacion_conexion_cs":
                        break;
                    case "respuesta_lista":
                        break;
                    case "emitir_cancion":
                        // recibir IP y puerto de cliente
                        (new ConexionCC(ConexionCC.Listen())).start();  // esto es bloqueante??
                        fout.writeObject(new Mensaje("preparado_cs", s.getLocalSocketAddress()));
                        break;
                    case "preparado_sc":
                        SocketAddress ipAddress = (SocketAddress) answer.getObject();

                        var both = ipAddress.toString().split(":");
                        (new ConexionCC(both[0], Integer.parseInt(both[1]))).start();
                        break;
                    case "desconexion_sc":
                        System.out.println("Server disconnected.");
                        // cerrar todos los recursos y terminar
                        break;
                    default:
                        // mensaje desconocido
                        break;
                }
            }

        } catch (Exception e) {
            System.err.printf("Server unreachable: %s\n", e.getMessage());
            System.exit(-1);
        }
    }
}
