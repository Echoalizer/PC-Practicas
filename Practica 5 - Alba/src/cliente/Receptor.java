package cliente;

import mensajes.Mensaje;
import mensajes.TipoMensaje;

public class Receptor extends Thread {

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
