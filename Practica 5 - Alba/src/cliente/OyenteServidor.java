package cliente;

import mensajes.ActualizarCancReceptor;
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
import java.net.SocketException;
import java.util.ArrayList;

public class OyenteServidor extends Thread {
    private final ObjectInputStream fin;
    private final ObjectOutputStream fout;

    private final SharedBuffer consola;

    private String name;
    private int puerto;
    
    private Usuario self;
 

    // throws IOException ya que si hay algún error, directamente no se crea el objeto
    public OyenteServidor(ObjectOutputStream fout, ObjectInputStream fin, SharedBuffer buffer, Usuario self) throws IOException {
        this.fin = fin;
        this.fout = fout;

        this.consola = buffer;
        
        this.self = self;
        this.name = self.getUsername();
    }

    @Override
    public void run() {
        boolean continua = true;

        Mensaje msg;
        String server = "server", sender = name, receiver;
        TipoMensaje tipo;

        try {

            while (Cliente.running && continua) {

                msg = (Mensaje) fin.readObject();

                tipo = msg.getTipo();
                sender = msg.getSender();
                receiver = msg.getReceiver();

                consola.enviar("DEBUG Recibido mensaje tipo %s\n".formatted(tipo));

                switch (tipo) {
                    case CONFIRMACION_CONEXION:
                        // El puerto se asigna en emisión -- porque?
                        consola.enviar("Se ha establecido conexion con el servidor\n");
                        break;

                    case RESPUESTA_LISTA_USUARIOS:
                        consola.enviar("Lista de usuarios\n");
                        ArrayList<Usuario> usuarios = (ArrayList<Usuario>) msg.getContent();
                        StringBuilder listaUsuarios = new StringBuilder();
                        for (Usuario u : usuarios) {
                            listaUsuarios.append(u).append("\n");
                        }
                        consola.enviar(listaUsuarios.toString() + "\n");
                        break;

                    case RESPUESTA_LISTA_CANCIONES:
                        consola.enviar("Lista de canciones\n");
                        ArrayList<Cancion> canciones = (ArrayList<Cancion>) msg.getContent();
                        StringBuilder listaCanciones = new StringBuilder();
                        for (Cancion c : canciones) {
                            listaCanciones.append(c).append("\n");
                        }
                        consola.enviar(listaCanciones.toString() + "\n");
                        break;

                    case EMITIR_CANCION:
                        Mensaje.Content data = (Mensaje.Content) msg.getContent();

                        new Emisor(Integer.parseInt(data.getAddress()), consola, self).start();
                        assert receiver.equals(this.name);
                        fout.writeObject(new PreparadoCS(receiver, sender, data.getAddress(), data.getId()));

                        break;

                    case PREPARADO_SC:
                        Mensaje.Content content = (Mensaje.Content) msg.getContent();

                        new Receptor(content.getAddress(), consola, content.getId(), self).start();

                        // TODO podemos usar Canal; pasar fout al Receptor
                        sleep(1000);  // a falta de sincronizar arriba y abajo

                        // can puede ser null si el receptor no ha completado su funcion

                        // actualizamos siempre, asumiendo que no ha habido error en la transmisión
                        Cancion can = self.getCancion(content.getId());
                        // Se manda al servidor un mensaje de que se quiere actualizar las canciones del cliente
                        fout.writeObject(new ActualizarCancReceptor(name, server, can));

                        break;

                    case CONFIRMACION_ACTUALIZACION_CANC:
                        this.consola.enviar("Se ha actualizado correctamente el servidor\n");
                        break;

                    case DESCONEXION:
                        consola.enviar("ERROR Se ha desconectado el servidor.\n");
                        continua = false;
                        break;

                    case RESPUESTA_COMPROBACION_SC:
                        Cancion c = (Cancion) msg.getContent();
                        if (c.getId().equals("error"))
                            this.consola.enviar("La cancion original no se ha podido añadir al cliente debido a que ya estaba en el servidor. Si se desea, se podria pedir al cliente que ya la tenga\n");
                        else {

                            //Ahora se va actualizar el usuario
                            this.self.addCancion(c);

                            this.consola.enviar("La cancion se añadio correctamente tanto al cliente como al servidor\n");
                        }
                        break;

                    default:
                        throw new OperationNotSupportedException("No existe el tipo de mensaje: %s.".formatted(tipo));
                }

            }
            if (!Cliente.running) {
                consola.enviar("BuEnooOOoOoooOOoo.\n");
            }

        } catch (SocketException e) {
            // La excepcion es el mensaje de cierre
        } catch (Exception e) {
            // este mensaje no es legible
            throw new RuntimeException(e);
        } finally {
            try {
                fin.close();
                fout.close();
            } catch (IOException e) {
                // no se pudo cerrar socket
            }
        }
    }
}
