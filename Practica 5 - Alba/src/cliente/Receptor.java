package cliente;

import mensajes.Mensaje;
import mensajes.TipoMensaje;

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

            while (true) {

                Mensaje msg = (Mensaje) fin.readObject();

                TipoMensaje tipo = msg.getTipo();

                switch (tipo) {
                    case CONFIRMACION_CONEXION:
                        System.out.println("Se ha establecido conexion con el p2p");
                        break;

                    case RESPUESTA_CANCION_CC:
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
