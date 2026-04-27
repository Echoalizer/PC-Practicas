package cliente;

import mensajes.ConfirmacionConexion;
import mensajes.Mensaje;
import mensajes.RespuestaCancion;
import mensajes.TipoMensaje;
import utils.Cancion;

import javax.naming.OperationNotSupportedException;
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

            boolean open = true;
            while (open) {

                Mensaje msg = (Mensaje) fin.readObject();

                TipoMensaje tipo = msg.getTipo();

                switch (tipo) {
                    case CONEXION_CC:
                        System.out.println("Se ha establecido conexion peer to peer");
                        this.fout.writeObject(new ConfirmacionConexion(null, null));
                        break;

                    case SOLICITUD_CANCION:
                        // obtener id
//                        Cancion c = user.get(id);
                        System.out.println("espero le guste....");
                        this.fout.writeObject(new RespuestaCancion(null, null, new Cancion("2", "la la la", "yo mismx")));
                        break;

                    case DESCONEXION:
                        open = false;
                        break;

                    default:
                        throw new OperationNotSupportedException("No existe el tipo de mensaje.");

                }
            }
            System.out.println("Se ha desconectado");
            this.fout.close();
            this.fin.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
