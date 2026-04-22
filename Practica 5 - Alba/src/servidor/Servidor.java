package servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


public class Servidor {

	private final int puerto;
	
	private final Socket s;
	private ObjectOutputStream fout;
	private ObjectInputStream fin;
	
	private Scanner in;
	
	
	public Servidor(int puerto,  Socket s, ObjectOutputStream fout, ObjectInputStream fin, Scanner in){
		this.puerto = puerto;
		this.s = s;
		this.fout= fout;
		this.fin = fin;
		this.in = in;
	}
	
	
	
	public static void main(String args[]) {

		Scanner in = new Scanner(System.in);
		//System.out.println("Introduce el puerto del servidor");
		//int puertoServidor = Integer.parseInt(in.nextLine());
		
		ServerSocket listen = null;
		Socket ss = null;
		try {
			listen = new ServerSocket(99);
			ss = listen.accept();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ObjectInputStream fin = null;
		ObjectOutputStream fout = null;
		
		
		try {
			fout = new ObjectOutputStream(ss.getOutputStream());
			fin = new ObjectInputStream(ss.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Servidor server = new Servidor(99, ss, fout, fin, in);
		OyenteCliente oyente = null;
		
		
		try {
			oyente = new OyenteCliente(ss, "OC1", fout, fin);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		oyente.start();
		server.run();
		
		
		
	}
	
	public void run() {
		
		System.out.println("El servidor se ha creado correctamente");
		
		
		menu();
		
		
		try {
			fin.close();
			fout.close();
			s.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	private void menu() {
		int option = 0;
		
		while(option != -1) {
			
			System.out.println("Escoge una de las opciones: ");
			System.out.println("1. ");
			
			String str = in.nextLine();
			option = Integer.parseInt(str);
			
			//switch(option) {}
			
		}
	}
	
	
	
}
