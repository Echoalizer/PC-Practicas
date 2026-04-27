package cliente;

import mensajes.Mensaje;
import mensajes.TipoMensaje;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Emisor extends Thread {

    private ObjectInputStream fin;
    private ObjectOutputStream fout;

    private final int port;

    public Emisor(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try (ServerSocket listen = new ServerSocket(port)) {
            System.out.println("POV: no camino");
            Socket s = listen.accept();
            System.out.println("que era bromaaaaa");
            this.fin = new ObjectInputStream(s.getInputStream());
            this.fout = new ObjectOutputStream(s.getOutputStream());
        } catch (Exception e) {
            throw new RuntimeException("no se pudo crear Emisor");
        }

        try {
            while (true) {

                Mensaje msg = (Mensaje) fin.readObject();

                TipoMensaje tipo = msg.getTipo();

                switch (tipo) {
                    case CONEXION_CC:
                        System.out.println("Se ha establecido conexion peer to peer");
                        break;

                    case SOLICITUD_CANCION_CC:
                        break;

                    case DESCONEXION_CC:
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
