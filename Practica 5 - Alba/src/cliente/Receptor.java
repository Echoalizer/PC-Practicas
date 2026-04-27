package cliente;

import mensajes.*;
import utils.Cancion;

import javax.naming.OperationNotSupportedException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Receptor extends Thread {

    private ObjectInputStream fin;
    private ObjectOutputStream fout;

    private final int port;

    public Receptor(String port) {
        this.port = Integer.parseInt(port);
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
                        System.out.println("Se ha establecido conexion con el p2p");
                        // no tenemos id cancion
                        this.fout.writeObject(new SolicitudCancion(null, null, null));
                        break;

                    case RESPUESTA_CANCION_CC:
                        Cancion cancion = (Cancion) msg.getContent();
                        System.out.println(cancion);
                        System.out.println("que chula!!! muchas gracias!!!");

                        this.fout.writeObject(new Desconexion(null, null));
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
