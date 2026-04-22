package servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import mensajes.ConfirmacionConexion;
import mensajes.ConfirmacionDesconexionCliente;
import mensajes.Mensaje;
import mensajes.tipoMensaje;

public class OyenteCliente extends Thread {

	private final String name;
	private final Socket s;
	
	private ObjectInputStream fin;
	private ObjectOutputStream fout;
	
	
	// Hace throws IOException ya que si hay algun error, el compilador directamente no crea el objeto 
	public OyenteCliente(Socket s, String name, ObjectOutputStream fout, ObjectInputStream fin) throws IOException{
		this.name = name;
		this.s = s;
		
		System.out.println("Se ha conectado el servidor");
		this.fin = fin;
		this.fout = fout;
			
	}
	
	
	@Override
	public void run(){
		
		try {
			while(true) {
				Mensaje msg = (Mensaje) fin.readObject();
				
				tipoMensaje tipo = msg.getTipo();
				
				switch(tipo) {
				case CONEXION:
					System.out.println("Se ha establecido conexion con el cliente");
					fout.writeObject(new ConfirmacionConexion());
					break;
				case DESCONEXION_CLIENTE:
					System.out.println("Se va a desconectar el cliente");
					fout.writeObject(new ConfirmacionDesconexionCliente());
					break;
					
				default:
					break;
				}
				
			}
			
			
			
		}
		catch (Exception e) {
            throw new RuntimeException(e);
        }
		
		
	}	
	
}
