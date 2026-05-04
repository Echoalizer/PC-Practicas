package servidor;

import concurrent.Canal;
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
    private final String name;
    private final int id;

    private final Socket s;
    private final Canal canalCliente;

    // el nombre puede ser confuso pero ayuda a la legibilidad en el switch de mensajes
    private final SharedBuffer consola;
    private final Servidor servidor;


    // throws IOException ya que si hay algún error, directamente no se crea el objeto
    public OyenteCliente(Socket s, int id, ObjectOutputStream fout, ObjectInputStream fin,
                         SharedBuffer buffer, Servidor srv
    ) throws IOException {
        this.name = "OC" + id;
        this.id = id;

        this.s = s;
        this.canalCliente = new Canal(fout, fin);

        this.consola = buffer;
        this.servidor = srv;
    }

    @Override
    public void run() {
        boolean continua = true;

        Mensaje msg;
        String server = "server", sender, receiver;
        TipoMensaje tipo;

        Canal canal;

        // try externo se encarga de tratar InterruptedException del productor-consumidor
        try {

            try {
                while (continua) {
                    msg = (Mensaje) canalCliente.read();

                    tipo = msg.getTipo();
                    sender = msg.getSender();
                    receiver = msg.getReceiver();

                    switch (tipo) {
                        case CONEXION_CS:
                            Usuario user = (Usuario) msg.getContent();
                            this.consola.enviar(name + " - Conexión establecida\n");
                            if (!this.servidor.anadirUsuario(user))
                                this.consola.enviar("El usuario %s ya existe.\n".formatted(user.getUsername()));
                            for (Cancion c : user.getCanciones()) {
                                this.servidor.anadirCancion(c);
                                this.servidor.update(c.getId(), user);
                            }
                            this.servidor.anadirCanal(user.getUsername(), canalCliente);
//                            int puerto = 991;
                            canalCliente.write(new ConfirmacionConexion(server, sender));
                            consola.enviar("DEBUG enviado mensaje a %s\n".formatted(name));
                            break;

                        case SOLICITUD_LISTA_USUARIOS:
                            ArrayList<Usuario> usuarios = this.servidor.getUsuarios();
                            canalCliente.write(new RespuestaListaUsuarios(server, sender, usuarios));
                            break;

                        case SOLICITUD_LISTA_CANCIONES:
                            ArrayList<Cancion> canciones = this.servidor.getCanciones();
                            canalCliente.write(new RespuestaListaCanciones(server, sender, canciones));
                            break;

                        case SOLICITUD_CANCION:
                            String cancion = (String) msg.getContent();
                            String propietario = this.servidor.getUsuarioCancion(cancion);
                            if (propietario == null) {
                                this.consola.enviar("ERROR %s - No existe la cancion %s.\n".formatted(name, cancion));
                            } else {
                                if (!sender.equals(receiver)) {
                                    this.consola.enviar(name + " - Solicitud de conexión: " + sender + " --- " + receiver + "\n");
                                    canal = this.servidor.getCanal(receiver);
//                                    String puerto = servidor.getPuerto(id);
                                    // TODO obtener puerto del cliente
                                    String puerto = "991";
                                    canal.write(new EmitirCancion(sender, propietario, puerto, cancion));
                                    // enviar error si esto falla ?
                                }
                                // else el cliente ha pedido una cancion que ya tiene
                            }
                            break;

                        case PREPARADO_CS:
                            Mensaje.Content content = (Mensaje.Content) msg.getContent();
                            String address = content.getAddress();  // address = puerto

                            canal = servidor.getCanal(receiver);
                            this.consola.enviar(name + " - Se creará conexión:  " + sender + " --- " + receiver + "\n");
                            canal.write(new PreparadoSC(sender, receiver, address, content.getId()));
                            break;

                        case ACTUALIZAR_CANC_RECEPTOR:
                        	this.consola.enviar("Se esta actualizando el servidor\n");

                            // En este punto, se asume que la canción ya está dentro de los usuarios receptor y emisor,
                            // por lo que no hay que comprobar aquí nada, simplemente hay que agregar dicha canción
                            // al cliente receptor
                        	Cancion c = (Cancion) msg.getContent();
                        	Usuario usuarioReceptor = servidor.getUsuario(sender);
                        	servidor.update(c.getId(), usuarioReceptor);

                            canalCliente.write(new ConfirmacionActualizacionCanc(server, sender));
                        	break;
                            
                            
                        case COMPROBAR_CANCION_CS:
                        	this.consola.enviar("Se va a comprobar si el servidor tiene ya esa cancion\n");
                        	Cancion canc = (Cancion) msg.getContent();
                        	boolean exist = servidor.checkCancion(canc.getId());

                            canal = servidor.getCanal(sender);
                        	if(!exist) {
                        		this.consola.enviar("La cancion no existe en el servidor, asi que se va a introducir a continuacion\n");
                        		servidor.anadirCancion(canc);
                        		
                        		Usuario u = servidor.getUsuario(sender);
                            	servidor.update(canc.getId(), u);
                                canalCliente.write(new RespuestaComprobacionCancionSC(canc, server, sender));

                        	}
                        	else {
                                this.consola.enviar("ERROR La cancion ya existia en el servidor\n");
                                // enviamos una objeto cancion falso
                        		Cancion cancError = new Cancion("error", null, null); 
                            	canal.write(new RespuestaComprobacionCancionSC(cancError, server, sender));
                        	}
                        	break;
                        
                        case DESCONEXION:
                            Usuario user2 = (Usuario) msg.getContent();
                            this.consola.enviar(name + " - Se ha desconectado el cliente\n");

//                            this.consola.enviar("borrando %s\n usuario\n".formatted(user2));
                            for (Cancion c2 : user2.getCanciones()) {
                                this.servidor.remove(c2.getId(), user2.getUsername());
//                                this.consola.enviar("DEBUG %s borrada\n".formatted(c2.getId()));
                            }
                            this.servidor.borrarUsuario(user2);
                            this.consola.enviar("DEBUG usuario %s borrado.\n".formatted(user2.getUsername()));
                            this.servidor.borrarCanal(user2.getUsername());
                            continua = false;
                            break;

                        default:
                            throw new OperationNotSupportedException("No existe el tipo de mensaje: %s.".formatted(tipo));
                    }
                }

            } catch (EOFException e) {
                this.consola.enviar("ERROR %s - El cliente se ha desconectado.\n".formatted(name));
            } catch (Exception e) {
                this.consola.enviar("ERROR %s - %s.\n".formatted(name, e.getMessage()));
            } finally {
                try {
                    canalCliente.close();
                    s.close();
                    this.consola.enviar("DEBUG cerrar ok - %s\n".formatted(name));
                } catch (IOException e) {
                    this.consola.enviar("ERROR No se han podido cerrar las conexiones.\n");
                }
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
