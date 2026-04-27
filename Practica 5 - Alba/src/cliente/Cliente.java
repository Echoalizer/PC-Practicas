package cliente;

import locks.LockId;
import locks.LockTicket;
import mensajes.*;
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

    private String name;

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
            puertoServidor = Integer.parseInt(in.nextLine());
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
                this.buffer.enviar("se ha producido un error: %s".formatted(e.getMessage()));
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }

        // Lo primero que va a hacer el cliente cuando se cree, es mandar un mensaje de que se ha conectado al servidor
        try {
//            String ip = s.getInetAddress().getHostAddress();
            this.buffer.enviar("Introduce tu nombre de usuario: ");
            String username = this.reader.nextLine();
            this.name = username;
            fout.writeObject(new Conexion(username, "server"));
        } catch (IOException e) {
            try {
                this.buffer.enviar("ERROR El cliente no ha podido mandar el mensaje de CONEXION");
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Menu
        menu();

        //Se cierra el socket y los canales
        try {
            fin.close();
            fout.close();
            s.close();
        } catch (IOException e) {
            try {
                this.buffer.enviar("se ha producido un error: %s".formatted(e.getMessage()));
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }

        // de esta manera no se quedan hilos colgando.
        // podriamos apagar el hilo OyenteServidor transmitiendo el mensaje de conexion cerrada.
        System.exit(0);
    }

    private void menu() {

        try {

            int option = 0;
            while (option != -1) {

                StringBuilder menu = new StringBuilder();
                menu.append("Escoge una de las opciones: ").append("\n");
                menu.append("1. DESCONEXION CLIENTE").append("\n");
                menu.append("2. LISTA USUARIOS").append("\n");
                menu.append("3. LISTA CANCIONES").append("\n");
                menu.append("4. SOLICITAR CANCION").append("\n");
                menu.append("5. AÑADIR CANCION").append("\n");

                buffer.enviar(menu.toString());

                option = reader.nextInt();

                try {

                    switch (option) {
                        case 1:
                            fout.writeObject(new DesconexionCliente(name, ""));

                            // Una vez se ha recibido por parte del servidor que se va a desconectar el cliente -> Se cambia de opcion
                            // para asi salir del bucle y cerrar los sockets y tod
                            option = -1;
                            break;
                        case 2:
                            System.out.println("Lista de usuarios");
                            fout.writeObject(new SolicitudListaUsuarios(name, "server"));
                            break;
                        case 3:
                            System.out.println("Lista de canciones");
                            fout.writeObject(new SolicitudListaCanciones(name, "server"));
                            break;
                        case 4:
                            System.out.print("Id de la cancion: ");
                            String id = reader.nextLine();

                            // receiver null o server
                            fout.writeObject(new SolicitudCancion(name, null, id));
                            break;
                        case 5:
                            System.out.print("Titulo: ");
                            String titulo = reader.nextLine();
                            System.out.print("Artista: ");
                            String artista = reader.nextLine();

                        default:
                            break;
                    }

                } catch (IOException e) {
                    System.err.printf("se ha producido un error: %s", e.getMessage());
                }
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            reader.close();
        } catch (Exception e) {
            System.err.println("no se pudo cerrar el scanner");
            throw new RuntimeException(e);
        }
    }
}
