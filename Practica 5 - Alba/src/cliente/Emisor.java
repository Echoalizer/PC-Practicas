package cliente;

import mensajes.ConfirmacionConexion;
import mensajes.Mensaje;
import mensajes.RespuestaCancion;
import mensajes.TipoMensaje;
import producersConsumers.SharedBuffer;
import utils.Cancion;
import utils.Usuario;

import javax.naming.OperationNotSupportedException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Emisor extends Thread {

    private ObjectInputStream fin;
    private ObjectOutputStream fout;

    SharedBuffer consola;

	private Usuario self;
	
    private final int port;

    public Emisor(int port, SharedBuffer buffer, Usuario self) {
        this.port = port;
        this.consola = buffer;
        
        this.self = self;
    }

    @Override
    public void run() {  // que pasa si dos clientes piden dato
        try (ServerSocket listen = new ServerSocket(port)) {
            Socket s = listen.accept();
            this.fin = new ObjectInputStream(s.getInputStream());
            this.fout = new ObjectOutputStream(s.getOutputStream());
        } catch (Exception e) {
            throw new RuntimeException("no se pudo crear Emisor");
        }

        try {

            boolean open = true;
            while (open) {

                Mensaje msg = (Mensaje) fin.readObject();

                TipoMensaje tipo = msg.getTipo();

                switch (tipo) {
                    case CONEXION_CC:
                        consola.enviar("Se ha establecido la conexion p2p.\n");
                        this.fout.writeObject(new ConfirmacionConexion(null, null, 0));
                        break;

                    case SOLICITUD_CANCION:
                        // obtener id
                        String id = (String) msg.getContent();
//                        Cancion c = user.get(id);
                        
                        //
                        
                        //Se va a comprobar si el emisor tiene la cancion con id que le ha pasado el receptor
                        
                        //Si no tiene esa cancion
                        if(!self.checkCancion(id)) {
                        	consola.enviar("La cancion cuyo id ha pasado el receptor, no corresponde con ninguna canción de las que tiene el emisor.\n");
                        }
                        //Si si tiene la cancion
                        else {
                        	consola.enviar("DEBUG envio de cancion\n");
                        	//!!!!!!!!!!Habria que hacer new Cancion()???
                        	Cancion c = self.getCancion(id);
                        	this.fout.writeObject(new RespuestaCancion(null, null, c));

                        }
                        	
                        //
                        break;

                    case DESCONEXION:
                        open = false;
                        break;

                    default:
                        throw new OperationNotSupportedException("No existe el tipo de mensaje.");

                }
            }
            consola.enviar("DEBUG Finalizada conexion p2p.\n");
            this.fout.close();
            this.fin.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
