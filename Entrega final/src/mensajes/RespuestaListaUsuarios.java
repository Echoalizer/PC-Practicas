package mensajes;

import utils.Usuario;

import java.util.ArrayList;


public class RespuestaListaUsuarios extends Mensaje {

    private final ArrayList<Usuario> lista;

    public RespuestaListaUsuarios(String sender, String receiver, ArrayList<Usuario> lista) {
        super(TipoMensaje.RESPUESTA_LISTA_USUARIOS, sender, receiver);
        this.lista = lista;
    }

    @Override
    public ArrayList<Usuario> getContent() {
        return this.lista;
    }

}
