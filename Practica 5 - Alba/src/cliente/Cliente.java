package cliente;

import locks.LockId;
import locks.LockTicket;
import mensajes.Conexion;
import mensajes.DesconexionCliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    private final Socket s;

    private final Scanner in;

    private final LockId oyenteLock;

    private final ObjectOutputStream fout;
    private final ObjectInputStream fin;

    public Cliente(Socket s, ObjectOutputStream fout, ObjectInputStream fin) {
        this.s = s;
        this.fout = fout;
        this.fin = fin;

        this.in = new Scanner(System.in);

        // lock para la terminal
        this.oyenteLock = new LockTicket();
    }

    // main
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);

        String IPServidor;
        int puertoServidor;

        // Hay que crear el nuevo cliente -> Por lo que hay que pedir su info y crear el oyenteServidor
        if (args.length > 0) {
            IPServidor = args[0];
            puertoServidor = Integer.parseInt(args[1]);
        } else {
            System.out.println("Introduce la IP del servidor");
            IPServidor = "localhost";

            System.out.println("Introduce el puerto del servidor");
            puertoServidor = Integer.parseInt(in.nextLine());
        }

        in.close();

        // Ahora creamos el socket que conecta con el servidor, y la instancia de Cliente

        Socket s;

        try {
            s = new Socket(IPServidor, puertoServidor);
        } catch (IOException e) {
            System.err.printf("error al conectar con el servidor: %s", e.getMessage());
            throw new RuntimeException(e);
        }

        ObjectOutputStream fout = null;
        ObjectInputStream fin = null;
        try {
            fout = new ObjectOutputStream(s.getOutputStream());
            fin = new ObjectInputStream(s.getInputStream());
        } catch (IOException e) {
            System.err.printf("error: no se ha podido crear el canal de comunicación. %s", e.getMessage());
        }

        Cliente cli = new Cliente(s, fout, fin);
        cli.run();
    }


    public void run() {
        try {
            OyenteServidor oyente = new OyenteServidor(fout, fin);
            oyente.start();
        } catch (IOException e) {
            System.err.printf("se ha producido un error: %s", e.getMessage());
        }

        // Lo primero que va a hacer el cliente cuando se cree, es mandar un mensaje de que se ha conectado al servidor
        try {
            fout.writeObject(new Conexion());
        } catch (IOException e) {
            System.err.println("El cliente no ha podido mandar el mensaje de CONEXION");
        }

        // Menu
        menu();

        //Se cierra el socket y los canales
        try {
            fin.close();
            fout.close();
            s.close();
            in.close();
        } catch (IOException e) {
            System.err.printf("se ha producido un error: %s", e.getMessage());
        }
    }


    private void menu() {

        int option = 0;
        while (option != -1) {

            System.out.println("Escoge una de las opciones: ");
            System.out.println("1. DESCONEXION CLIENTE");
            String str = in.nextLine();
            option = Integer.parseInt(str);

            switch (option) {
                case 1: // DESCONEXION CLIENTE
                    System.out.println("El cliente se quiere desconectar");
                    try {
                        fout.writeObject(new DesconexionCliente());
                    } catch (IOException e) {
                        System.err.printf("se ha producido un error: %s", e.getMessage());
                    }


                    // Una vez se ha recibido por parte del servidor que se va a desconectar el cliente -> Se cambia de opcion
                    // para asi salir del bucle y cerrar los sockets y tod
                    option = -1;
            }
        }
    }
}
