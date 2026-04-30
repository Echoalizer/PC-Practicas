package cliente;

import mensajes.ActualizarCancReceptor;
import mensajes.Mensaje;
import mensajes.PreparadoCS;
import mensajes.TipoMensaje;
import producersConsumers.SharedBuffer;
import utils.Cancion;
import utils.Usuario;

import javax.naming.OperationNotSupportedException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class OyenteServidor extends Thread {
    private final ObjectInputStream fin;
    private final ObjectOutputStream fout;

    private final SharedBuffer consola;

    private String name;
    private int puerto;
    
    private Usuario self;
 

    // throws IOException ya que si hay algún error, directamente no se crea el objeto
    public OyenteServidor(ObjectOutputStream fout, ObjectInputStream fin, SharedBuffer buffer, int puerto, Usuario self) throws IOException {
        this.fin = fin;
        this.fout = fout;

        this.consola = buffer;
        this.puerto = puerto;
        
        this.self = self;
        
        //Donde se inicializa name?
        
    }

    @Override
    public void run() {
        boolean continua = true;
        boolean listening = true;  // dependiente del thread Cliente.run()

        Mensaje msg;
        String server = "server", sender = name, receiver;
        TipoMensaje tipo;

        try {

            while (listening && continua) {

                msg = (Mensaje) fin.readObject();

                tipo = msg.getTipo();
                sender = msg.getSender();
                receiver = msg.getReceiver();

                switch (tipo) {
                    case CONFIRMACION_CONEXION:
                        consola.enviar("Se ha establecido conexion con el servidor");
                        break;

                    case RESPUESTA_LISTA_USUARIOS:
                        consola.enviar("Lista de usuarios");
                        ArrayList<Usuario> usuarios = (ArrayList<Usuario>) msg.getContent();
                        StringBuilder listaUsuarios = new StringBuilder();
                        for (Usuario u : usuarios) {
                            listaUsuarios.append(u).append("\n");
                        }
                        consola.enviar(listaUsuarios.toString());
                        break;

                    case RESPUESTA_LISTA_CANCIONES:
                        consola.enviar("Lista de canciones");
                        ArrayList<Cancion> canciones = (ArrayList<Cancion>) msg.getContent();
                        StringBuilder listaCanciones = new StringBuilder();
                        for (Cancion c : canciones) {
                            listaCanciones.append(c).append("\n");
                        }
                        consola.enviar(listaCanciones.toString());
                        break;

                    case EMITIR_CANCION:
                        new Emisor(puerto, consola, self).start();
                        // assert receiver == this.name --!-- no tenemos el name de Cliente
                        fout.writeObject(new PreparadoCS(receiver, sender, "" + puerto));

                        break;

                    case PREPARADO_SC:
                        String address = (String) msg.getContent();
                        // añadir id de cancion
                        boolean before = self.checkCancion(address);
                        new Receptor(address, consola, "", self).start();                        
                        boolean after = self.checkCancion(address);
                        
                        if(!before && after) {
                        	//Aqui tendria que hacerse lo de nueva cancion??
                        	Cancion c = self.getCancion(address);
                        	//Se manda al servidor un mensaje de que se quiere actualizar las canciones del cliente
                        	fout.writeObject(new ActualizarCancReceptor(sender, server,c ));
                        }
                        break;

                    case CONFIRMACION_ACTUALIZACION_CANC:
                    	this.consola.enviar("Se ha actualizado correctamente el servidor");
                        break;
                        
                    case DESCONEXION:
                        consola.enviar("ERROR Se ha desconectado el servidor.");
                        continua = false;
                        break;

                    case RESPUESTA_COMPROBACION_SC:
                    	Cancion c = (Cancion) msg.getContent();
                    	if(c.getId().equals("error"))
                    		this.consola.enviar("La cancion original no se ha podido añadir al cliente debido a que ya estaba en el servidor. Si se desea, se podria pedir al cliente que ya la tenga\n");
                    	else {
                    		
                    		//Ahora se va actualizar el usuario
                    		this.self.addCancion(c);
                    		
                    		this.consola.enviar("La cancion se añadio correctamente tanto al cliente como al servidor");
                    	}
                    		
                        
                    default:
                        throw new OperationNotSupportedException("No existe el tipo de mensaje.");
                }

            }
            try {  // en finally?
                fin.close();
                fout.close();
            } catch (IOException e) {
                consola.enviar("ERROR no se pudo cerrar el socket");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
