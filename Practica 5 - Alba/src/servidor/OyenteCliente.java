package servidor;

import mensajes.*;
import producersConsumers.SharedBuffer;
import utils.Cancion;
import utils.Usuario;

import javax.naming.OperationNotSupportedException;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class OyenteCliente extends Thread {

    private final int id;
    private final String name;

    private final Socket s;
    private final ObjectInputStream fin;
    private final ObjectOutputStream fout;

    // el nombre puede ser confuso pero ayuda a la legibilidad en el switch de mensajes
    private final SharedBuffer consola;

    private final Servidor servidor;

    // throws IOException ya que si hay algún error, directamente no se crea el objeto
    public OyenteCliente(Socket s, int id, ObjectOutputStream fout, ObjectInputStream fin,
                         SharedBuffer buffer, Servidor srv
    ) throws IOException {
        this.id = id;
        this.name = "OC" + id;

        this.s = s;
        this.fin = fin;
        this.fout = fout;

        this.consola = buffer;
        this.servidor = srv;
    }

    @Override
    public void run() {
        boolean continua = true;

        Mensaje msg;
        String server = "server", sender, receiver;
        TipoMensaje tipo;

        ObjectOutputStream cout;

        // try externo se encarga de tratar InterruptedException del productor-consumidor
        try {

            try {
                while (continua) {
                    msg = (Mensaje) fin.readObject();

                    tipo = msg.getTipo();
                    sender = msg.getSender();
                    receiver = msg.getReceiver();

                    switch (tipo) {
                        case CONEXION_CS:
                            this.consola.enviar(name + " - Conexión establecida");
                            fout.writeObject(new ConfirmacionConexion(server, sender));
                            break;

                        case SOLICITUD_LISTA_USUARIOS:
                            ArrayList<Usuario> usuarios = this.servidor.getUsuarios();
                            fout.writeObject(new RespuestaListaUsuarios(server, sender, usuarios));
                            break;

                        case SOLICITUD_LISTA_CANCIONES:
                            ArrayList<Cancion> canciones = this.servidor.getCanciones();
                            fout.writeObject(new RespuestaListaCanciones(server, sender, canciones));
                            break;

                        case SOLICITUD_CANCION:
                            String cancion = (String) msg.getContent();
                            Usuario propietario = this.servidor.getUsuarioCancion(cancion);
                            receiver = propietario.getUsername();
                            this.consola.enviar(name + " - Solicitud de conexión: " + sender + " --- " + receiver);
                            cout = this.servidor.getCanal(receiver);
                            cout.writeObject(new EmitirCancion(sender, receiver));
                            break;

                        case PREPARADO_CS:
                            cout = servidor.getCanal(receiver);
                            this.consola.enviar(name + " - Se creará conexión:  " + sender + " --- " + receiver);
                            cout.writeObject(new PreparadoSC(sender, receiver));
                            break;

                        case DESCONEXION_CS:
                            this.consola.enviar(name + " - Se ha desconectado el cliente");
                            continua = false;
                            break;

                        default:
                            throw new OperationNotSupportedException("No existe el tipo de mensaje.");
                    }
                }

            } catch (EOFException e) {
                this.consola.enviar("ERROR %s - El cliente se ha desconectado.".formatted(name));
            } catch (Exception e) {
                this.consola.enviar("ERROR %s - %s.".formatted(name, e.getMessage()));
            } finally {
                try {
                    fin.close();
                    fout.close();
                    s.close();
                    this.consola.enviar("DEBUG cerrar ok - %s".formatted(name));
                } catch (IOException e) {
                    this.consola.enviar("ERROR No se han podido cerrar las conexiones.");
                }
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
