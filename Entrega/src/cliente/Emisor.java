package cliente;

import mensajes.ConfirmacionConexion;
import mensajes.Mensaje;
import mensajes.RespuestaCancion;
import mensajes.TipoMensaje;
import producersConsumers.SharedBuffer;
import utils.Cancion;

import javax.naming.OperationNotSupportedException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Emisor extends Thread {

    private ObjectInputStream fin;
    private ObjectOutputStream fout;

    SharedBuffer consola;

    private final int port;

    public Emisor(int port, SharedBuffer buffer) {
        this.port = port;
        this.consola = buffer;
    }

    @Override
    public void run() {
        try (ServerSocket listen = new ServerSocket(port)) {
            Socket s = listen.accept();
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
                        consola.enviar("Se ha establecido la conexion p2p\n");
                        this.fout.writeObject(new ConfirmacionConexion(null, null));
                        break;

                    case SOLICITUD_CANCION:
                        // obtener id
//                        Cancion c = user.get(id);
                        consola.enviar("DEBUG envio de cancion\n");
                        this.fout.writeObject(new RespuestaCancion(null, null, new Cancion("215", "Hello", "Adele")));
                        break;

                    case DESCONEXION:
                        open = false;
                        break;

                    default:
                        throw new OperationNotSupportedException("No existe el tipo de mensaje.");

                }
            }
            consola.enviar("DEBUG Finalizada conexion p2p\n");
            this.fout.close();
            this.fin.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
