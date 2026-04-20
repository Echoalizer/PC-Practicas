package app;

import model.Entero;
import model.Mensaje;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class OyenteCliente extends Thread {
    private final String name;
    private final InetAddress clientIpAddress;
    private final Socket s;

    private final Almacen almacen;
    private final Canales canales;

    private ObjectOutputStream fout;
    private ObjectInputStream fin;

    OyenteCliente(Socket s, Almacen al, Canales c) throws IOException {
        this.s = s;
        this.almacen = al;
        this.canales = c;
        this.clientIpAddress = s.getInetAddress();
        name = "oyente@" + clientIpAddress.getHostAddress() + ":" + s.getPort();
        try {
            fin = new ObjectInputStream(s.getInputStream());
            // Object streams normally (but not always) auto-flush
            fout = new ObjectOutputStream(s.getOutputStream());

            canales.save(clientIpAddress, fin);
            canales.save(clientIpAddress, fout);
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
                    case "conexion_cs":
                        String userId = (String) msg.getObject();
                        almacen.postUser(userId, clientIpAddress);
                        fout.writeObject(new Mensaje("confirmacion_conexion"));
                        break;
                    case "solicitud_lista":
                        fout.writeObject(new Mensaje("respuesta_lista", almacen.getLista()));
                        break;
                    case "solicitud_cancion":
                        String cancion = (String) msg.getObject();
                        var usuario = almacen.getOwner(cancion);
                        var canal = canales.get(usuario);
                        canal.writeObject(new Mensaje("emitir_cancion"));
                        break;
                    case "preparado_cs":
                        // mensaje contiene IP, puerto destino de ambos
                        // envia a c1
                        String str = (String) msg.getObject();
                        Entero k = new Entero(Integer.parseInt(str));

                        fout.writeObject(new Mensaje("devolver", k)); // del servidor
                        break;
                    case "desconexion_cs":
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
