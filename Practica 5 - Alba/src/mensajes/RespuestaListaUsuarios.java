package mensajes;

import utils.Usuario;

import java.util.HashSet;


public class RespuestaListaUsuarios extends Mensaje {

    private final HashSet<Usuario> lista;

    public RespuestaListaUsuarios(String sender, String receiver, HashSet<Usuario> lista) {
        super(TipoMensaje.RESPUESTA_LISTA_USUARIOS, sender, receiver);
        this.lista = lista;
    }

    @Override
    public HashSet<Usuario> getContent() {
        return this.lista;
    }

}
