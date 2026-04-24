package servidor;

import locks.LockId;
import locks.LockTicket;
import mensajes.Mensaje;
import utils.Cancion;
import utils.Usuario;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


public class Servidor {

    private final int puerto;
    private final ServerSocket s;

    // este era para la consola, usar LockTicket en oyenteServidor
    private final LockId oyenteLock;
//    private final LockId socketLock;

    private final ListaConcurrente<Usuario> usuarios;
    private final ListaConcurrente<Cancion> canciones;
    private final MapaCancionesUsuarios canciones_por_usuario;

//    private final Map<Usuario, ObjectOutputStream> canales;

    public Servidor(int puerto, ServerSocket s) {
        this.puerto = puerto;
        this.s = s;

        this.usuarios = new ListaConcurrente<>();
        this.canciones = new ListaConcurrente<>();
        this.canciones_por_usuario = new MapaCancionesUsuarios();

        this.oyenteLock = new LockTicket();
    }

    public void run() throws IOException {

        Socket ss = null;
        int k = 0;
        while (true) {
            ss = s.accept();
            k++;

            ObjectInputStream fin = null;
            ObjectOutputStream fout = null;

            try {
                fout = new ObjectOutputStream(ss.getOutputStream());
                fin = new ObjectInputStream(ss.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                OyenteCliente oyente = new OyenteCliente(ss, k, fout, fin, this, oyenteLock);
                oyente.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void enviar(Usuario user, Mensaje mensaje) {
        // mover a oyenteCliente
//        var fout = this.canales.get(user);
//        socketLock.takeLock(0);
//        fout.println(mensaje);
//        socketLock.releaseLock(0);
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

        System.out.println("Introduce el puerto del servidor");
        int puertoServidor = Integer.parseInt(in.nextLine());

        in.close();

        try {
            ServerSocket listen = new ServerSocket(puertoServidor);
            Servidor servidor = new Servidor(puertoServidor, listen);
            System.out.println("El servidor se ha creado correctamente");
            servidor.run();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
