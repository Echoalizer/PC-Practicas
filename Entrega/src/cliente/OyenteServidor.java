package cliente;

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
    private final ObjectInputStream fin;
    private final ObjectOutputStream fout;

    private final SharedBuffer consola;

    private String name;

    // throws IOException ya que si hay algún error, directamente no se crea el objeto
    public OyenteServidor(ObjectOutputStream fout, ObjectInputStream fin, SharedBuffer buffer) throws IOException {
        this.fin = fin;
        this.fout = fout;

        this.consola = buffer;
    }

    @Override
    public void run() {
        boolean continua = true;
        boolean listening = true;  // dependiente del thread Cliente.run()

        Mensaje msg;
        String server = "server", sender = name, receiver;
        TipoMensaje tipo;

        try {

            while (listening && continua) {

                msg = (Mensaje) fin.readObject();

                tipo = msg.getTipo();
                sender = msg.getSender();
                receiver = msg.getReceiver();

                switch (tipo) {
                    case CONFIRMACION_CONEXION:
                        consola.enviar("Se ha establecido conexion con el servidor\n");
                        break;

                    case RESPUESTA_LISTA_USUARIOS:
                        consola.enviar("Lista de usuarios\n");
                        ArrayList<Usuario> usuarios = (ArrayList<Usuario>) msg.getContent();
                        StringBuilder listaUsuarios = new StringBuilder();
                        for (Usuario u : usuarios) {
                            listaUsuarios.append(u).append("\n");
                        }
                        consola.enviar(listaUsuarios.toString());
                        break;

                    case RESPUESTA_LISTA_CANCIONES:
                        consola.enviar("Lista de canciones\n");
                        ArrayList<Cancion> canciones = (ArrayList<Cancion>) msg.getContent();
                        StringBuilder listaCanciones = new StringBuilder();
                        for (Cancion c : canciones) {
                            listaCanciones.append(c).append("\n");
                        }
                        consola.enviar(listaCanciones.toString());
                        break;

                    case EMITIR_CANCION:
                        int port = 991;  // magic number
                        new Emisor(port, consola).start();
                        // assert receiver == this.name --!-- no tenemos el name de Cliente
                        fout.writeObject(new PreparadoCS(receiver, sender, "" + port));

                        break;

                    case PREPARADO_SC:
                        String address = (String) msg.getContent();
                        new Receptor(address, consola).start();
                        break;

                    case DESCONEXION:
                        consola.enviar("ERROR Se ha desconectado el servidor.\n");
                        continua = false;
                        break;

                    default:
                        throw new OperationNotSupportedException("No existe el tipo de mensaje.");
                }

            }
            try {  // en finally?
                fin.close();
                fout.close();
            } catch (IOException e) {
                consola.enviar("ERROR no se pudo cerrar el socket\n");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
