package app;

import model.Musica;
import model.Usuario;

import java.net.SocketAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Almacen {
    private final RW controller;

    // lista de usuarios
    private final Set<Usuario> usuarios;
    // Informacion (musica) que tiene cada usuario
    private final Map<Musica, Usuario> mapa_informacion;

    public Almacen(RW controller) {
        this.controller = controller;

        usuarios = new HashSet<>();
        mapa_informacion = new HashMap<>();
    }

    public Set<Musica> getLista() {
        Set<Musica> lista;
        try {
            controller.request_read();
            lista = mapa_informacion.keySet();
            controller.release_read();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }

    public Usuario getOwner(String cancion) {
        Usuario owner;
        try {
            controller.request_read();
            Musica m = mapa_informacion.keySet().stream().filter(c -> c.getTitulo().equals(cancion)).findFirst().orElse(null);

            owner = mapa_informacion.get(m);

            controller.release_read();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return owner;
    }

    public void postUser(String userId, SocketAddress clientIP) {
        try {
            controller.request_write();
            Usuario user = usuarios.stream().filter(u -> u.getId().equals(userId)).findFirst().orElse(null);
            if (user == null) {
                user = new Usuario(userId, clientIP);
                usuarios.add(user);
            } else {
                user.setIpAddress(clientIP);
            }
            for (var dato : user.getSharedData()) {
                mapa_informacion.put(dato, user);
            }
            controller.release_write();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
