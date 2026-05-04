package servidor;

import concurrent.Canal;
import concurrent.Consola;
import concurrent.ListaConcurrente;
import concurrent.MapaConcurrente;
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

    private final SharedBuffer buffer;

    private final ListaConcurrente<Usuario> usuarios;
    private final ListaConcurrente<Cancion> canciones;

    // TODO permitir varios usuarios
    // canciones indexadas por username
    private final MapaConcurrente<ArrayList<String>> canciones_por_usuario;

    private final MapaConcurrente<Canal> canales;

//    private final ArrayList<LockedString> puertos;


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
            // no necesitamos usar el buffer de la consola en main porque se ejecuta sin concurrencia
            System.out.print("Introduce el puerto del servidor: ");
            puertoServidor = in.nextInt();
        }

        in.close();

        try {
            ServerSocket listen = new ServerSocket(puertoServidor);  // nunca se cierra
            Servidor servidor = new Servidor(listen);
            // no necesitamos usar el buffer de la consola en main porque se ejecuta sin concurrencia
            System.out.println("El servidor se ha creado correctamente.");
            servidor.run();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void run() throws IOException {
        Socket ss;
        int k = 0;  // TODO concurrente para volver a bajar el numero de cliente
        // while (alive)
        while (true) {
            ss = srvSocket.accept();
            k++;

            try {
                ObjectOutputStream fout = new ObjectOutputStream(ss.getOutputStream());
                ObjectInputStream fin = new ObjectInputStream(ss.getInputStream());

                OyenteCliente oyente = new OyenteCliente(ss, k, fout, fin, buffer, this);

                oyente.start();
            } catch (IOException e) {
                // TODO fix catch
                try {
                    this.buffer.enviar("ERROR Error al conectar con el cliente: %s\n".formatted(e.getMessage()));
                } catch (InterruptedException ex) {
                    throw new RuntimeException("Error al almacenar en el buffer de consola!");
                }
            }
        }
    }

    public ArrayList<Usuario> getUsuarios() throws InterruptedException {
        return this.usuarios.leerLista();  // re-throw
    }


    // Metodo necesario para cuando queremos actualizar canciones_por_usuario
    public Usuario getUsuario(String name) {
    	Usuario sol = null;
    	
    	try {
			for (Usuario s : usuarios.leerLista() ) {
			    if (s.getUsername().equals(name)) {
			        sol = s;
			        break; 
			    }
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
    	return sol;
    }
    
    
    public boolean checkCancion(String id) {
    	boolean exist = false;
    	
    	try {
			for (Cancion c : canciones.leerLista() ) {
			    if (c.getId().equals(id)) {
			        exist = true;
			        break; 
			    }
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
    	return exist;
    }

    public boolean anadirUsuario(Usuario usuario) throws InterruptedException {
        return this.usuarios.escribir(usuario);  // re-throw
    }

    public boolean borrarUsuario(Usuario usuario) throws InterruptedException {
        return this.usuarios.borrar(usuario);
    }

    public ArrayList<Cancion> getCanciones() throws InterruptedException {
        return this.canciones.leerLista();  // re-throw
    }

    public boolean anadirCancion(Cancion cancion) throws InterruptedException {
        return this.canciones.escribir(cancion);  // re-throw
    }

    public boolean borrarCancion(Cancion cancion) throws InterruptedException {
        return this.canciones.borrar(cancion);
    }

    public String getUsuarioCancion(String cancion) throws InterruptedException {
//        var lista = this.getUsuarioCancion(cancion);
        var usuarios = this.canciones_por_usuario.leer(cancion);

        return usuarios == null ? null : usuarios.get(0);

    }

    public void update(String cancion, Usuario usuario) throws IOException, InterruptedException {
        var lista = this.canciones_por_usuario.leer(cancion);
        if (lista == null) lista = new ArrayList<>();

        lista.add(usuario.getUsername());
        this.canciones_por_usuario.escribir(cancion, lista);
        this.usuarios.borrar(usuario);
        usuario.addCancion(new Cancion(cancion, null, null));
        this.anadirUsuario(usuario);
    }

    public void remove(String cancion, String usuario) throws InterruptedException {
        var usuarios = this.canciones_por_usuario.leer(cancion);
        usuarios.remove(usuario);
        this.canciones_por_usuario.borrar(cancion);
        if (usuarios.isEmpty()) {
            this.canciones.borrar(new Cancion(cancion, null, null));
        } else {
            this.canciones_por_usuario.escribir(cancion, usuarios);
        }
    }

    public Canal getCanal(String username) throws InterruptedException {
        return this.canales.leer(username);
    }

    public boolean anadirCanal(String username, Canal canal) throws InterruptedException, IOException {
        return this.canales.escribir(username, canal);
    }

    public boolean borrarCanal(String username) throws InterruptedException {
        return this.canales.borrar(username);
    }
}
