package servidor;

import locks.LockId;
import mensajes.ConfirmacionConexion;
import mensajes.ConfirmacionDesconexionCliente;
import mensajes.Mensaje;
import mensajes.TipoMensaje;
import utils.Cancion;
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

    private final ListaConcurrente<Usuario> usuarios;
    private final ListaConcurrente<Cancion> canciones;
    private final MapaCancionesUsuarios canciones_por_usuario;

    private final LockId logLock;

    // Hace throws IOException ya que si hay algun error, el compilador directamente no crea el objeto
    public OyenteCliente(Socket s, int id, ObjectOutputStream fout, ObjectInputStream fin,
                         ListaConcurrente<Usuario> usuarios, ListaConcurrente<Cancion> canciones,
                         MapaCancionesUsuarios mapa, LockId lock
    ) throws IOException {
        this.id = id;
        this.name = "OC" + id;
        this.s = s;
        System.out.println("Se ha conectado el servidor");
        this.fin = fin;
        this.fout = fout;

        this.usuarios = usuarios;
        this.canciones = canciones;
        this.canciones_por_usuario = mapa;
        this.logLock = lock;
    }

    @Override
    public void run() {

        try {
            while (true) {
                Mensaje msg = (Mensaje) fin.readObject();

                TipoMensaje tipo = msg.getTipo();

                switch (tipo) {
                    case CONEXION:
                        logLock.takeLock(id);
                        System.out.println("Se ha establecido conexion con el cliente");
                        logLock.releaseLock(id);

                        fout.writeObject(new ConfirmacionConexion());
                        break;
                    case DESCONEXION_CLIENTE:

                        logLock.takeLock(id);
                        System.out.println("Se va a desconectar el cliente");
                        logLock.releaseLock(id);

                        fout.writeObject(new ConfirmacionDesconexionCliente());
                        break;

                    case RESPUESTA_LISTA_USUARIOS:
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
