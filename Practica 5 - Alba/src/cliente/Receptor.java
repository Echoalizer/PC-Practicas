package cliente;

import mensajes.*;
import producersConsumers.SharedBuffer;
import utils.Cancion;

import javax.naming.OperationNotSupportedException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Receptor extends Thread {

    private ObjectInputStream fin;
    private ObjectOutputStream fout;

    SharedBuffer consola;

    private final int port;
    private final String idCancion;

    public Receptor(String port, SharedBuffer buffer, String idCancion) {
        this.port = Integer.parseInt(port);
        this.consola = buffer;
        this.idCancion = idCancion;
    }

    @Override
    public void run() {
        try {
            Socket s = new Socket("localhost", port);
            this.fout = new ObjectOutputStream(s.getOutputStream());
            this.fin = new ObjectInputStream(s.getInputStream());
        } catch (Exception e) {
            throw new RuntimeException("no se pudo crear Receptor");
        }

        try {

            fout.writeObject(new ConexionCC(null, null));

            boolean open = true;
            while (open) {

                Mensaje msg = (Mensaje) fin.readObject();

                TipoMensaje tipo = msg.getTipo();

                switch (tipo) {
                    case CONFIRMACION_CONEXION:
                        consola.enviar("Se ha establecido la conexion p2p");
                        // no tenemos id cancion
                        this.fout.writeObject(new SolicitudCancion(null, null, idCancion));
                        break;

                    case RESPUESTA_CANCION_CC:
                        Cancion cancion = (Cancion) msg.getContent();
                        consola.enviar("DEBUG" + cancion);
                        // guardar cancion en el usuario
                        // y actualizar en serv
                        this.fout.writeObject(new Desconexion(null, null));
                        open = false;
                        break;

                    default:
                        throw new OperationNotSupportedException("No existe el tipo de mensaje.");

                }
            }
            consola.enviar("DEBUG Finalizada conexion p2p");
            this.fout.close();
            this.fin.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
