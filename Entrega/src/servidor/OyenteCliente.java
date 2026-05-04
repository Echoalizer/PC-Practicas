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

        Canal cout;

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
                                    cout = this.servidor.getCanal(receiver);
                                    cout.write(new EmitirCancion(sender, receiver));
                                }
                                // else el cliente ha pedido una cancion que ya tiene
                            }
                            break;

                        case PREPARADO_CS:
                            String address = (String) msg.getContent();
                            cout = servidor.getCanal(receiver);
                            this.consola.enviar(name + " - Se creará conexión:  " + sender + " --- " + receiver);
                            cout.write(new PreparadoSC(sender, receiver, address));
                            break;

                        case DESCONEXION:
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
