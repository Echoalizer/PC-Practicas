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

    private Canal canal;

    SharedBuffer consola;

    private final String addr;
    private final String idCancion;
    private final String name;

    private final Canal canalServ;

    private Usuario self;


    public Receptor(String addr, SharedBuffer buffer, Canal canalServ, String idCancion, Usuario self) {
        this.addr = addr;
        this.consola = buffer;
        this.idCancion = idCancion;
        this.self = self;
        this.name = self.getUsername();
        this.canalServ = canalServ;
    }

    @Override
    public void run() {
        try {

            String[] splitAddress = this.addr.split(":");
            String address = splitAddress[0];
            int port = Integer.parseInt(splitAddress[1]);
            consola.enviar("DEBUG Conectando con %s:%d\n".formatted(address, port));

            Socket s = new Socket(address, port);

            this.canal = new Canal(
                    new ObjectOutputStream(s.getOutputStream()),
                    new ObjectInputStream(s.getInputStream()));

        } catch (Exception e) {
            throw new RuntimeException("no se pudo crear Receptor: %s".formatted(e.getMessage()));
        }

        try {
            consola.enviar("DEBUG receptor listo\n");

            canal.write(new ConexionCC(name, null));

            boolean open = true;
            while (open) {

                Mensaje msg = (Mensaje) canal.read();

                TipoMensaje tipo = msg.getTipo();
                String sender = msg.getSender();

                switch (tipo) {
                    case CONFIRMACION_CONEXION:
                        consola.enviar("Se ha establecido la conexion p2p\n");
                        // no tenemos id cancion
                        this.canal.write(new SolicitudCancion(name, sender, idCancion));
                        break;

                    case RESPUESTA_CANCION_CC:
                        Cancion cancion = (Cancion) msg.getContent();
                        consola.enviar("DEBUG Recibida: %s \n".formatted(cancion.toString()));
                        // guardar cancion en el usuario
                        self.addCancion(cancion);

                        // Se manda al servidor un mensaje de que se quiere actualizar las canciones del cliente
                        this.canalServ.write(new ActualizarCancReceptor(name, "server", cancion));

                        // mensaje desconexionCC?
                        this.canal.write(new Desconexion(name, sender, null));
                        open = false;
                        break;

                    default:
                        throw new OperationNotSupportedException("No existe el tipo de mensaje.");

                }
            }
            consola.enviar("DEBUG Finalizada conexion p2p\n");
            this.canal.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
