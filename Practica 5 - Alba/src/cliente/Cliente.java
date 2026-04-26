package cliente;

import locks.LockId;
import locks.LockTicket;
import mensajes.Conexion;
import mensajes.DesconexionCliente;
import producersConsumers.SharedBuffer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    private final Socket s;

    private final Scanner reader;

    private final LockId oyenteLock;

    private final ObjectOutputStream fout;
    private final ObjectInputStream fin;

    private final SharedBuffer buffer;

    public Cliente(Socket s, ObjectOutputStream fout, ObjectInputStream fin, Scanner in) {
        this.s = s;
        this.fout = fout;
        this.fin = fin;
        this.reader = in;

        this.buffer = new SharedBuffer(2);  // tamaño del buffer
        new Consola(buffer).start();

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
            // este codigo no envia mensajes a la consola porque se ejecuta de forma secuencial
            System.out.print("Introduce la IP del servidor: ");
            IPServidor = in.nextLine();

            System.out.print("Introduce el puerto del servidor: ");
            puertoServidor = in.nextInt();  // Integer.parseInt(in.nextLine()) ?
        }

        // Ahora creamos el socket que conecta con el servidor, y la instancia de Cliente

        Socket s = null;
        try {
            s = new Socket(IPServidor, puertoServidor);
        } catch (IOException e) {
            System.err.printf("Error al conectar con el servidor: %s\n", e.getMessage());
            System.exit(-1);
        }

        ObjectInputStream fin = null;
        ObjectOutputStream fout = null;
        try {
            fin = new ObjectInputStream(s.getInputStream());
            fout = new ObjectOutputStream(s.getOutputStream());
        } catch (IOException e) {
            System.err.printf("error: no se ha podido crear el canal de comunicación. %s", e.getMessage());
        }

        // a partir de aquí la consola se encarga de escribir los mensajes
        Cliente cli = new Cliente(s, fout, fin, in);
        cli.run();
    }

    // hace de productor al enviar mensajes a la consola
    public void run() {
        try {
            OyenteServidor oyente = new OyenteServidor(fout, fin, buffer);
            oyente.start();
        } catch (IOException e) {
            try {
                this.buffer.almacenar("se ha producido un error");
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
//            System.err.printf("se ha producido un error: %s", e.getMessage());
        }

        // Lo primero que va a hacer el cliente cuando se cree, es mandar un mensaje de que se ha conectado al servidor
        try {
            fout.writeObject(new Conexion("", ""));
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
        } catch (IOException e) {
            System.err.printf("se ha producido un error: %s", e.getMessage());
        }
    }


    private void menu() {

        int option = 0;
        while (option != -1) {

            System.out.println("Escoge una de las opciones: ");
            System.out.println("1. DESCONEXION CLIENTE");

            option = reader.nextInt();

            switch (option) {
                case 1: // DESCONEXION CLIENTE
                    System.out.println("El cliente se quiere desconectar");
                    try {
                        fout.writeObject(new DesconexionCliente("", ""));
                    } catch (IOException e) {
                        System.err.printf("se ha producido un error: %s", e.getMessage());
                    }


                    // Una vez se ha recibido por parte del servidor que se va a desconectar el cliente -> Se cambia de opcion
                    // para asi salir del bucle y cerrar los sockets y tod
                    option = -1;
                    break;
                default:
                    break;
            }
        }

        try {
            reader.close();
        } catch (Exception e) {
            System.err.println("no se pudo cerrar el scanner");
            throw new RuntimeException(e);
        }
    }
}
