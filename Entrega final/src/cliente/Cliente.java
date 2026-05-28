package cliente;

import concurrent.Canal;
import concurrent.Consola;
import mensajes.*;
import producersConsumers.SharedBuffer;
import utils.Cancion;
import utils.Usuario;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;


public class Cliente {
    private final Socket s;
    private final Canal canal;

    private final Scanner reader;
    private final SharedBuffer consola;

    private Usuario self;
    private String name;

    // TODO proteger con lock
    public static volatile boolean running = true;

    public Cliente(Socket s, ObjectOutputStream fout, ObjectInputStream fin, Scanner in) {
        this.s = s;
        this.canal = new Canal(fout, fin);
        this.reader = in;

        this.consola = new SharedBuffer(2);  // tamaño del buffer
        new Consola(consola).start();
    }


    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);

        String IPServidor;
        int puertoServidor;

        // Hay que crear el nuevo cliente -> Por lo que hay que pedir su info y crear el oyenteServidor
        if (args.length > 1) {
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
            List<Inet4Address> networkAddrs = getAddresses();
            this.consola.enviar("DEBUG IPs de la interfaz de red: %s\n".formatted(networkAddrs));

            String ip = s.getLocalSocketAddress().toString();

            String netIp = networkAddrs.stream()
                    .filter((a) -> a.getHostAddress().startsWith("10.")
                            || a.getHostAddress().startsWith("172.16.")
                            || a.getHostAddress().startsWith("192.168."))
                    .findFirst().orElse((Inet4Address) s.getLocalAddress()).getHostAddress();

            this.consola.enviar("DEBUG Cliente conectado desde %s (local %s)\n".formatted(netIp, ip));
            this.consola.enviar("Introduce tu nombre de usuario: ");
            String username = this.reader.nextLine();
            this.self = new Usuario(username, ip);
            self.addCancion(new Cancion("%d".formatted(2 * username.hashCode()), username, username));
            this.name = username;

            // comprobar que no se envia el mensaje antes de terminar de crear oyente
            OyenteServidor oyente = new OyenteServidor(canal, consola, self, netIp);
            oyente.start();

            // Lo primero que va a hacer el cliente cuando se cree, es mandar un mensaje de que se ha conectado al servidor
            canal.write(new Conexion(ip, "server", self));
        } catch (IOException e) {
            try {
                this.consola.enviar("ERROR El cliente no ha podido mandar el mensaje de CONEXION\n");
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
            canal.close();
            s.close();
        } catch (IOException e) {
            try {
                this.consola.enviar("se ha producido un error: %s\n".formatted(e.getMessage()));
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }

        // de esta manera no se quedan hilos colgando.
        // podriamos apagar el hilo OyenteServidor transmitiendo el mensaje de conexion cerrada.
        System.exit(0);
    }

    private static List<Inet4Address> getAddresses() throws SocketException {
        List<Inet4Address> networkAddrs = new ArrayList<>();
        var netInterfaces = NetworkInterface.getNetworkInterfaces();
        while (netInterfaces.hasMoreElements()) {
            NetworkInterface nInterface = netInterfaces.nextElement();
            Enumeration<InetAddress> addresses = nInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress addr = addresses.nextElement();
                if (addr instanceof Inet4Address) {
                    networkAddrs.add((Inet4Address) addr);
                }
            }
        }
        return networkAddrs;
    }

    private void menu() {

        try {

            int option = 0;
            while (option != -1) {

                StringBuilder menu = new StringBuilder();
                menu.append("\n");
                menu.append("Escoge una de las opciones: ").append("\n");
                menu.append("1. DESCONEXION CLIENTE").append("\n");
                menu.append("2. LISTA USUARIOS").append("\n");
                menu.append("3. LISTA CANCIONES SERVER").append("\n");
                menu.append("4. LISTA CANCIONES PROPIAS").append("\n");
                menu.append("5. SOLICITAR CANCION").append("\n");
                menu.append("6. AÑADIR CANCION").append("\n");
                menu.append("\n");

                consola.enviar(menu.toString());

                String server = "server";

                try {
                    option = Integer.parseInt(reader.nextLine());

                    switch (option) {
                        case 1:
                            canal.write(new Desconexion(name, server, self));

                            // Una vez se ha recibido por parte del servidor que se va a desconectar el cliente -> Se cambia de opcion
                            // para asi salir del bucle y cerrar los sockets y tod
                            option = -1;
                            running = false;
                            break;
                        case 2:
                            canal.write(new SolicitudListaUsuarios(name, server));
                            break;
                        case 3:
                            canal.write(new SolicitudListaCanciones(name, server));
                            break;
                        case 4:
                            consola.enviar("Lista de canciones propias: \n");
                            consola.enviar(self.getCanciones().toString() + "\n\n");
                            break;
                        case 5:
                            consola.enviar("Id de la cancion: ");
                            String id = reader.nextLine();
                            canal.write(new SolicitudCancion(name, server, id)); // receiver null porque va dirigido a un cliente que aun no conocemos
                            break;
                        case 6:
                            consola.enviar("Titulo: ");
                            String titulo = reader.nextLine();
                            consola.enviar("Artista: ");
                            String artista = reader.nextLine();
//                            // id auto-generado
                            String idCancion = "" + (titulo.hashCode() + artista.hashCode());
                            //Hay que comprobar que esa cancion no este ya en el servidor 
                            Cancion c = new Cancion(idCancion, titulo, artista);
                            canal.write(new ComprobarCancionCS(c, name, server));
                            break;

                        default:
                            // opción sin funcionalidad asignada
                            break;
                    }

                } catch (NumberFormatException e) {
                    consola.enviar("Comando no reconocido\n");
                    option = 0;
                } catch (SocketException e) {
                    consola.enviar("No se puede comunicar con el servidor.\n");
                    option = -1;
                    running = false;
                } catch (IOException e) {
                    consola.enviar("ERROR se ha producido un error: %s\n".formatted(e));
                }
            }

            try {
                reader.close();
            } catch (Exception e) {
                consola.enviar("ERROR no se pudo cerrar el scanner\n");
                throw new RuntimeException(e);
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
