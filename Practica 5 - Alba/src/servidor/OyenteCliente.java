package servidor;

import mensajes.ConfirmacionConexion;
import mensajes.Mensaje;
import mensajes.TipoMensaje;
import producersConsumers.SharedBuffer;

import javax.naming.OperationNotSupportedException;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class OyenteCliente extends Thread {

    private final int id;
    private final String name;
    private final Socket s;

    private final ObjectInputStream fin;
    private final ObjectOutputStream fout;

    // el nombre puede ser confuso pero ayuda a la legibilidad en el swith de mensajes
    private final SharedBuffer consola;

    // throws IOException ya que si hay algún error, directamente no se crea el objeto
    public OyenteCliente(Socket s, int id, ObjectOutputStream fout, ObjectInputStream fin,
                         SharedBuffer buffer
    ) throws IOException {
        this.id = id;
        this.name = "OC" + id;
        this.s = s;
        this.fin = fin;
        this.fout = fout;

        this.consola = buffer;
    }

    @Override
    public void run() {
        boolean continua = true;

        try {
            while (continua) {
                Mensaje msg = (Mensaje) fin.readObject();

                //
                //

                TipoMensaje tipo = msg.getTipo();

                switch (tipo) {
                    case CONEXION_CS:
                        // productor-consumidor para la consola
                        this.consola.almacenar(name + " - Conexión establecida");

                        // hay que añadir emisor y receptor al mensaje
                        fout.writeObject(new ConfirmacionConexion("", ""));
                        break;

                    case SOLICITUD_LISTA_USUARIOS:
                        break;

                    case SOLICITUD_LISTA_CANCIONES:
                        break;

                    case SOLICITUD_CANCION:
//                        Usuario receptor = this.servidor.getUsuarioCancion("");

//                        var fout = this.canales.get(user);
//                        socketLock.takeLock(0);
//                        fout.println(mensaje);
//                        socketLock.releaseLock(0);

                        break;

                    case PREPARADO_CS:

                        break;

                    case DESCONEXION_CS:
                        this.consola.almacenar(name + " - Se ha desconectado el cliente");
                        continua = false;
                        break;

                    default:
                        throw new OperationNotSupportedException("No existe el tipo de mensaje.");
                }
            }

        } catch (EOFException e) {
            System.err.printf("%s - ERROR: El cliente se ha desconectado.\n", name);
        } catch (Exception e) {
            System.err.printf("%s - ERROR: %s.\n", name, e.getMessage());
        } finally {
            try {
                fin.close();
                fout.close();
                s.close();
                System.out.println("DEBUG cerrar ok");
            } catch (IOException e) {
                System.err.println("No se han podido cerrar las conexiones");
            }
        }

    }
}
