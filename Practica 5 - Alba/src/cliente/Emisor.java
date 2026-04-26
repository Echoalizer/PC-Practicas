package cliente;

import mensajes.Mensaje;
import mensajes.TipoMensaje;

public class Emisor extends Thread {


    @Override
    public void run() {
        try {

            while (true) {

                Mensaje msg = (Mensaje) fin.readObject();

                TipoMensaje tipo = msg.getTipo();

                switch (tipo) {
                    case CONEXION_CC:
                        System.out.println("Se ha establecido conexion peer to peer");
                        break;

                    case SOLICITUD_CANCION_CC:
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
