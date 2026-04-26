package cliente;

import locks.LockId;
import locks.LockTicket;
import mensajes.Mensaje;
import mensajes.TipoMensaje;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class OyenteServidor extends Thread {

    private int id;
    private String name;
    private ObjectInputStream fin;
    private ObjectOutputStream fout;

    // para controlar accceso a los canales de oyenteServidor y emisor
    private LockId socketLock;

    // throws IOException ya que si hay algún error, directamente no se crea el objeto
    public OyenteServidor(ObjectOutputStream fout, ObjectInputStream fin) throws IOException {
        this.fin = fin;
        this.fout = fout;

        this.socketLock = new LockTicket();
    }

    @Override
    public void run() {
        try {

            while (true) {

                Mensaje msg = (Mensaje) fin.readObject();

                TipoMensaje tipo = msg.getTipo();

                switch (tipo) {
                    case CONFIRMACION_CONEXION:
                        System.out.println("Se ha establecido conexion con el servidor");
                        break;

                    case RESPUESTA_LISTA_USUARIOS:
                        break;

                    case RESPUESTA_LISTA_CANCIONES:
                        break;

                    case EMITIR_CANCION:
                        break;

                    case PREPARADO_SC:
                        break;

                    case DESCONEXION_SC:
                        break;

                    default:
                        break;
                }

            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
