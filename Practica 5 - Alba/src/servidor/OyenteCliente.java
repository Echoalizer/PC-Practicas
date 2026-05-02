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
                            this.consola.enviar(name + " - Conexión establecida");
                            if (!this.servidor.anadirUsuario(user))
                                this.consola.enviar("El usuario %s ya existe.".formatted(user.getUsername()));
                            for (Cancion c : user.getCanciones()) {
                                this.servidor.anadirCancion(c);
                                this.servidor.update(c.getId(), user);
                            }
                            this.servidor.anadirCanal(user.getUsername(), canalCliente);
//                            int puerto = 991;
                            canalCliente.write(new ConfirmacionConexion(server, sender));
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
                            Usuario propietario = this.servidor.getUsuarioCancion(cancion);
                            if (propietario == null) {
                                this.consola.enviar("ERROR %s - No existe la cancion %s.".formatted(name, cancion));
                            } else {

                                receiver = propietario.getUsername();
                                if (!sender.equals(receiver)) {
                                    this.consola.enviar(name + " - Solicitud de conexión: " + sender + " --- " + receiver);
                                    canal = this.servidor.getCanal(receiver);
                                    String puerto = servidor.getPuerto(id);
                                    canal.write(new EmitirCancion(sender, receiver, puerto, cancion));
                                }
                                // else el cliente ha pedido una cancion que ya tiene
                            }
                            break;

                        case PREPARADO_CS:
                            Mensaje.Content content = (Mensaje.Content) msg.getContent();
                            String address = content.getAddress();  // address = puerto

                            canal = servidor.getCanal(receiver);
                            this.consola.enviar(name + " - Se creará conexión:  " + sender + " --- " + receiver);
                            canal.write(new PreparadoSC(sender, receiver, address, content.getId()));
                            break;

                        case ACTUALIZAR_CANC_RECEPTOR:
                        	this.consola.enviar("Se esta actualizando el servidor\n");
                        	
                        	//En este punto, se asume que la cancion ya esta dentro de los usuarios receptor y emisor, por lo que no hay que comprobar aqui nada, simplemente
                        	//hay que agregar dicha cancion al cliente receptor
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
                        		this.consola.enviar("ERROR: La cancion ya existia en el servidor\n");
                                // enviamos una objeto cancion falso
                        		Cancion cancError = new Cancion("error", null, null); 
                            	canal.write(new RespuestaComprobacionCancionSC(cancError, server, sender));
                        		
                        	}
                        	break;
                        
                        case DESCONEXION:
                            this.consola.enviar(name + " - Se ha desconectado el cliente");
                            continua = false;
                            break;

                        default:
                            throw new OperationNotSupportedException("No existe el tipo de mensaje: %s.".formatted(tipo));
                    }
                }

            } catch (EOFException e) {
                this.consola.enviar("ERROR %s - El cliente se ha desconectado.".formatted(name));
            } catch (Exception e) {
                this.consola.enviar("ERROR %s - %s.".formatted(name, e.getMessage()));
            } finally {
                try {
                    canalCliente.close();
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
