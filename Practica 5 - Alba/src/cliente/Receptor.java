package cliente;

import mensajes.Mensaje;
import mensajes.TipoMensaje;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Receptor extends Thread {

    private final ObjectInputStream fin;
    private final ObjectOutputStream fout;

    public Receptor(ObjectInputStream fin, ObjectOutputStream fout) {
        this.fin = fin;
        this.fout = fout;
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

                    case RESPUESTA_CANCION_CC:
                        break;

                    case DESCONEXION_CC:
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
