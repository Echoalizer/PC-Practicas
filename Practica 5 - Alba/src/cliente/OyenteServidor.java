package cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import mensajes.Conexion;
import mensajes.ConfirmacionConexion;
import mensajes.ConfirmacionDesconexionCliente;
import mensajes.Mensaje;
import mensajes.tipoMensaje;

public class OyenteServidor extends Thread{

	private final String name;
	private ObjectInputStream fin;
	private ObjectOutputStream fout;
	private int desconectado;
	
	// Hace throws IOException ya que si hay algun error, el compilador directamente no crea el objeto 
	public OyenteServidor(String name, ObjectOutputStream fout , ObjectInputStream fin) throws IOException{
		this.name = name;
		this.fin =fin;
		this.fout =fout;
		
		this.desconectado = 0;
	}
	
	
	@Override 
	public void run(){
		try {

			while(true) {
				
				Mensaje msg = (Mensaje) fin.readObject();
				
				tipoMensaje tipo = msg.getTipo();
				
				switch(tipo) {
					case CONFIRMACION_CONEXION:
						System.out.println("Se ha establecido conexion con el servidor");
						break;
					case CONFIRMACION_DESCONEXION_CLIENTE:
						System.out.println("Se ha desconectado al cliente");
						this.desconectado = 1;
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
	
	
	public int getDesconectado() {
		return this.desconectado;
	}
	
}
