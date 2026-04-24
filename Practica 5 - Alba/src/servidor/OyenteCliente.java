package servidor;

import locks.LockId;
import mensajes.ConfirmacionConexion;
import mensajes.ConfirmacionDesconexionCliente;
import mensajes.Mensaje;
import mensajes.TipoMensaje;
import utils.Usuario;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class OyenteCliente extends Thread {

    private final int id;
    private final String name;
    private final Socket s;

    private ObjectInputStream fin;
    private ObjectOutputStream fout;

    private Servidor servidor;

    private final LockId logLock;

    // Hace throws IOException ya que si hay algun error, el compilador directamente no crea el objeto
    public OyenteCliente(Socket s, int id, ObjectOutputStream fout, ObjectInputStream fin,
                         Servidor servidor, LockId lock
    ) throws IOException {
        this.id = id;
        this.name = "OC" + id;
        this.s = s;
        System.out.println("Se ha conectado el servidor");
        this.fin = fin;
        this.fout = fout;

        this.logLock = lock;
        this.servidor = servidor;
    }

    @Override
    public void run() {

        try {
            while (true) {
                Mensaje msg = (Mensaje) fin.readObject();

                //
                //

                TipoMensaje tipo = msg.getTipo();

                switch (tipo) {
                    case CONEXION:
                        // productor-consumidor para la consola
                        logLock.takeLock(0);
                        System.out.println("Se ha establecido conexion con el cliente");
                        logLock.releaseLock(0);

                        fout.writeObject(new ConfirmacionConexion());
                        break;

                    case DESCONEXION_CLIENTE:
                        logLock.takeLock(0);
                        System.out.println("Se va a desconectar el cliente");
                        logLock.releaseLock(0);

                        fout.writeObject(new ConfirmacionDesconexionCliente());
                        break;

                    case RESPUESTA_LISTA_USUARIOS:
                        break;

                    case SOLICITUD_CANCION:
                        Usuario receptor = this.servidor.getUsuarioCancion("");
                        this.servidor.enviar(receptor, new ConfirmacionConexion());
                        break;

                    case PREPARADO_CS:

                        break;
                    default:
                        break;
                }

            }
            // close socket, fin, fout


        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

}
