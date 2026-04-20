package app;

import model.Mensaje;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketAddress;

public class OyenteCliente extends Thread {
    private final String name;
    private final SocketAddress clientIpAddress;
    private final Socket s;

    private final Almacen almacen;
    private final Canales canales;

    private ObjectOutputStream fout;
    private ObjectInputStream fin;

    OyenteCliente(Socket s, Almacen al, Canales c) throws IOException {
        this.s = s;
        this.almacen = al;
        this.canales = c;
        this.clientIpAddress = s.getLocalSocketAddress();
        name = "oyente@" + clientIpAddress;
        try {
            fin = new ObjectInputStream(s.getInputStream());
            // Object streams normally (but not always) auto-flush
            fout = new ObjectOutputStream(s.getOutputStream());

//            canales.save(clientIpAddress, fin);
            canales.save(clientIpAddress, fout);
        } catch (IOException e) {
            System.err.printf("Read failed: %s", e.getMessage());
            System.exit(-1);
        }
    }

    @Override
    public void run() {
        try {
            listen:
            while (true) {  // label used to break loop
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

                        fout.writeObject(new Mensaje("devolver")); // del servidor
                        break;
                    case "desconexion_cs":
                        fout.close();
                        fin.close();
                        s.close();
                        break listen;
                    default:
                        // mensaje desconocido
                        break;
                }
            }
        } catch (EOFException e) {
            System.err.printf("Client '%s' disconnected abruptly.\n", name);
        } catch (IOException | ClassNotFoundException e) {
            System.err.printf("Client '%s' ended with error: %s\n", name, e.getMessage());
        }
    }
}
