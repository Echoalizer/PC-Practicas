package servidor;

import locks.LockId;
import locks.LockTicket;
import utils.Cancion;
import utils.Usuario;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


public class Servidor {

    private final ServerSocket srvSocket;

    private final ListaConcurrente<Usuario> usuarios;
    private final ListaConcurrente<Cancion> canciones;
    private final MapaCancionesUsuarios canciones_por_usuario;

    // este era para la consola, usar LockTicket en oyenteServidor
    private final LockId oyenteLock;

//    private final Map<Usuario, ObjectOutputStream> canales;

    public Servidor(ServerSocket s) {
        this.srvSocket = s;

        this.usuarios = new ListaConcurrente<>();
        this.canciones = new ListaConcurrente<>();
        this.canciones_por_usuario = new MapaCancionesUsuarios();

        this.oyenteLock = new LockTicket();
    }

    public void run() throws IOException {
        Socket ss;
        int k = 0;
        // permitir parar el bucle
        while (true) {
            ss = srvSocket.accept();
            k++;

            try {
                ObjectOutputStream fout = new ObjectOutputStream(ss.getOutputStream());
                ObjectInputStream fin = new ObjectInputStream(ss.getInputStream());

                OyenteCliente oyente = new OyenteCliente(ss, k, fout, fin, this, oyenteLock);

                oyente.start();
            } catch (IOException e) {
                System.err.printf("Error al conectar con el cliente: %s\n", e.getMessage());
            }
        }
    }

    public ListaConcurrente<Usuario> getUsuarios() {
        return this.usuarios;
    }

    public ListaConcurrente<Cancion> getCanciones() {
        return this.canciones;
    }

    public Usuario getUsuarioCancion(String cancion) throws InterruptedException {
        return this.canciones_por_usuario.leer(cancion);
    }

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);

        int puertoServidor;
        if (args.length > 0)
            puertoServidor = Integer.parseInt(args[0]);
        else {
            System.out.print("Introduce el puerto del servidor: ");
            puertoServidor = in.nextInt();
        }

        in.close();

        try {
            ServerSocket listen = new ServerSocket(puertoServidor);  // nunca se cierra
            Servidor servidor = new Servidor(listen);
            // proteger con lock
            System.out.println("El servidor se ha creado correctamente.");
            servidor.run();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
