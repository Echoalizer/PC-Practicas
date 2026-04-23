package cliente;

import mensajes.Mensaje;
import mensajes.TipoMensaje;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class OyenteServidor extends Thread {

    private final String name;
    private ObjectInputStream fin;
    private ObjectOutputStream fout;
    private boolean conectado;

    // Hace throws IOException ya que si hay algun error, el compilador directamente no crea el objeto
    public OyenteServidor(String name, ObjectOutputStream fout, ObjectInputStream fin) throws IOException {
        this.name = name;
        this.fin = fin;
        this.fout = fout;

        this.conectado = true;
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
                    case CONFIRMACION_DESCONEXION_CLIENTE:
                        System.out.println("Se ha desconectado al cliente");
                        this.conectado = false;
                        break;
                    default:
                        break;
                }

            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    public boolean getDesconectado() {
        return this.conectado;
    }

}
