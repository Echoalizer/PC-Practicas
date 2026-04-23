package mensajes;

import utils.Musica;

import java.util.Set;

public class RespuestaListaCanciones extends Mensaje {

    private Set<Musica> lista;

    public RespuestaListaCanciones(Set<Musica> lista) {
        super(TipoMensaje.RESPUESTA_LISTA_CANCIONES);
        this.lista = lista;
    }

    public Set<Musica> getLista() {
        return lista;
    }

}
