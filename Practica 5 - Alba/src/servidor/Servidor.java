package servidor;

import cliente.Consola;
import producersConsumers.SharedBuffer;
import utils.Cancion;
import utils.Usuario;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;


public class Servidor {

    private final ServerSocket srvSocket;

    private final ListaConcurrente<Usuario> usuarios;
    private final ListaConcurrente<Cancion> canciones;
    // canciones indexadas por username
    private final MapaConcurrente<Usuario> canciones_por_usuario;

    private final MapaConcurrente<Canal> canales;

    private final SharedBuffer buffer;


    public Servidor(ServerSocket s) {
        this.srvSocket = s;

        this.usuarios = new ListaConcurrente<>();
        this.canciones = new ListaConcurrente<>();
        this.canciones_por_usuario = new MapaConcurrente<>();

        this.canales = new MapaConcurrente<>();

        this.buffer = new SharedBuffer(2);  // tamaño del buffer
        new Consola(buffer).start();
    }


    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);

        int puertoServidor;
        if (args.length > 0)
            puertoServidor = Integer.parseInt(args[0]);
        else {
            System.out.print("Introduce el puerto del servidor: ");
            // no necesitamos usar el buffer de la consola en main porque se ejecuta sin concurrencia
            puertoServidor = in.nextInt();
        }

        in.close();

        try {
            ServerSocket listen = new ServerSocket(puertoServidor);  // nunca se cierra
            Servidor servidor = new Servidor(listen);
            System.out.println("El servidor se ha creado correctamente.");
            // no necesitamos usar el buffer de la consola en main porque se ejecuta sin concurrencia
            servidor.run();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() throws IOException {
        Socket ss;
        int k = 0;  // concurrente para volver a bajar el numero de cliente
        // permitir parar el bucle
        while (true) {
            ss = srvSocket.accept();
            k++;

            try {
                ObjectOutputStream fout = new ObjectOutputStream(ss.getOutputStream());
                ObjectInputStream fin = new ObjectInputStream(ss.getInputStream());

                OyenteCliente oyente = new OyenteCliente(ss, k, fout, fin, buffer, this);

                oyente.start();
            } catch (IOException e) {
                try {
                    this.buffer.enviar("ERROR Error al conectar con el cliente: %s".formatted(e.getMessage()));
                } catch (InterruptedException ex) {
                    System.err.println("Error al almacenar en el buffer de consola!");
                }
            }
        }
    }

    public ArrayList<Usuario> getUsuarios() throws InterruptedException {
        return this.usuarios.leerLista();
    }

    public boolean anadirUsuario(Usuario usuario) throws InterruptedException {
        return this.usuarios.escribir(usuario);
    }

    public ArrayList<Cancion> getCanciones() throws InterruptedException {
        return this.canciones.leerLista();
    }

    public boolean anadirCancion(Cancion cancion) throws InterruptedException {
        return this.canciones.escribir(cancion);
    }

    public Usuario getUsuarioCancion(String cancion) throws InterruptedException {
        return this.canciones_por_usuario.leer(cancion);
    }

    public void update(String cancion, Usuario usuario) throws IOException, InterruptedException {
        this.canciones_por_usuario.escribir(cancion, usuario);
    }

    public boolean anadirCanal(String username, Canal canal) throws InterruptedException, IOException {
        return this.canales.escribir(username, canal);
    }

    public Canal getCanal(String username) throws InterruptedException {
        return this.canales.leer(username);
    }
}
