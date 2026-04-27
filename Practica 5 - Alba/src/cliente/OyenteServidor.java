package cliente;

import locks.LockId;
import locks.LockTicket;
import mensajes.Mensaje;
import mensajes.PreparadoCS;
import mensajes.TipoMensaje;
import producersConsumers.SharedBuffer;
import utils.Cancion;
import utils.Usuario;

import javax.naming.OperationNotSupportedException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class OyenteServidor extends Thread {

    private final SharedBuffer consola;

    private final ObjectInputStream fin;
    private final ObjectOutputStream fout;

    private String name;

    // para controlar accceso a los canales de oyenteServidor y emisor
    private LockId socketLock;

    // throws IOException ya que si hay algún error, directamente no se crea el objeto
    public OyenteServidor(ObjectOutputStream fout, ObjectInputStream fin, SharedBuffer buffer) throws IOException {
        this.fin = fin;
        this.fout = fout;

        this.consola = buffer;

        this.socketLock = new LockTicket();
    }

    @Override
    public void run() {
        boolean continua = true;
        boolean listening = true;  // dependiente del thread Cliente.run()

        Mensaje msg;
        String server = "server", sender = name, receiver;
        TipoMensaje tipo;

        ObjectOutputStream cout;

        try {

            while (listening && continua) {

                msg = (Mensaje) fin.readObject();

                tipo = msg.getTipo();
                sender = msg.getSender();
                receiver = msg.getReceiver();

                switch (tipo) {
                    case CONFIRMACION_CONEXION:
                        consola.enviar("Se ha establecido conexion con el servidor");
                        break;

                    case RESPUESTA_LISTA_USUARIOS:
                        ArrayList<Usuario> usuarios = (ArrayList<Usuario>) msg.getContent();
                        StringBuilder listaUsuarios = new StringBuilder();
                        for (Usuario u : usuarios) {
                            listaUsuarios.append(u).append("\n");
                        }
                        consola.enviar(listaUsuarios.toString());
                        break;

                    case RESPUESTA_LISTA_CANCIONES:
                        ArrayList<Cancion> canciones = (ArrayList<Cancion>) msg.getContent();
                        StringBuilder listaCanciones = new StringBuilder();
                        for (Cancion c : canciones) {
                            listaCanciones.append(c).append("\n");
                        }
                        consola.enviar(listaCanciones.toString());
                        break;

                    case EMITIR_CANCION:
                        // crear thread emisor
                        // assert receiver == this.name --!-- no tenemos el name de Cliente
                        fout.writeObject(new PreparadoCS(receiver, sender));

                        break;

                    case PREPARADO_SC:
                        // crear thread receptor
                        break;

                    case DESCONEXION_SC:
                        consola.enviar("ERROR Se ha desconectado el servidor.");
                        continua = false;
                        break;

                    default:
                        throw new OperationNotSupportedException("No existe el tipo de mensaje.");
                }

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                fin.close();
                fout.close();
            } catch (IOException e) {
//                System.err.println("no se pudo cerrar el socket");
            }
        }
    }
}
