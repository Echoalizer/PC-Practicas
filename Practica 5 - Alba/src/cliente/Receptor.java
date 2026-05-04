package cliente;

import concurrent.Canal;
import mensajes.*;
import producersConsumers.SharedBuffer;
import utils.Cancion;
import utils.Usuario;

import javax.naming.OperationNotSupportedException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Receptor extends Thread {

    private ObjectInputStream fin;
    private ObjectOutputStream fout;

    SharedBuffer consola;

    private final int port;
    private final String idCancion;
    private final String name;

    private final Canal canalServ;

    private Usuario self;


    public Receptor(String port, SharedBuffer buffer, Canal canalServ, String idCancion, Usuario self) {
        this.port = Integer.parseInt(port);
        this.consola = buffer;
        this.idCancion = idCancion;
        this.self = self;
        this.name = self.getUsername();
        this.canalServ = canalServ;
    }

    @Override
    public void run() {
        try {
            Socket s = new Socket("localhost", port);
            this.fout = new ObjectOutputStream(s.getOutputStream());
            this.fin = new ObjectInputStream(s.getInputStream());
        } catch (Exception e) {
            throw new RuntimeException("no se pudo crear Receptor: %s".formatted(e.getMessage()));
        }

        try {
            consola.enviar("DEBUG receptor listo\n");

            fout.writeObject(new ConexionCC(name, null));

            boolean open = true;
            while (open) {

                Mensaje msg = (Mensaje) fin.readObject();

                TipoMensaje tipo = msg.getTipo();
                String sender = msg.getSender();

                switch (tipo) {
                    case CONFIRMACION_CONEXION:
                        consola.enviar("Se ha establecido la conexion p2p\n");
                        // no tenemos id cancion
                        this.fout.writeObject(new SolicitudCancion(name, sender, idCancion));
                        break;

                    case RESPUESTA_CANCION_CC:
                        Cancion cancion = (Cancion) msg.getContent();
                        consola.enviar("DEBUG Recibida: %s \n".formatted(cancion.toString()));
                        // guardar cancion en el usuario
                        self.addCancion(cancion);

                        // Se manda al servidor un mensaje de que se quiere actualizar las canciones del cliente
                        this.canalServ.write(new ActualizarCancReceptor(name, "server", cancion));

                        // mensaje desconexionCC?
                        this.fout.writeObject(new Desconexion(name, sender, null));
                        open = false;
                        break;

                    default:
                        throw new OperationNotSupportedException("No existe el tipo de mensaje.");

                }
            }
            consola.enviar("DEBUG Finalizada conexion p2p\n");
            this.fout.close();
            this.fin.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
