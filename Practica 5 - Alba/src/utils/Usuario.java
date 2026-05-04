package utils;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


public class Usuario implements Serializable {
    private String username;
    private String ipAddress;
    private Set<Cancion> canciones;

    public Usuario(String username, String ip) {
        this.username = username;
        this.ipAddress = ip;

        this.canciones = new HashSet<>();
    }

    public String getUsername() {
        return username;
    }

    public String getIp() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Set<Cancion> getCanciones() {
        return canciones;
    }

    public void addCancion(Cancion cancion) {
    	//Si la cancion no esta en el set del usuario 
    	if(!checkCancion(cancion.getId())) {
    		//Se agrega la cancion al set del usuario
    		this.canciones.add(cancion);
    	}
    }

    
    public boolean checkCancion(String id) {
    	return canciones.contains(new Cancion(id, null, null));
    }
    
    public Cancion getCancion(String id) {
    	Cancion sol = null;
    	
    	for (Cancion c : canciones) {
    	    if (c.getId().equals(id)) {
    	        sol = c;
    	        break; 
    	    }
    	}
    	
    	return sol;
	}
    
    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != this.getClass())
            return false;
        return this.username.equals(((Usuario) obj).username);
    }

    @Override
    public String toString() {
        return String.format("%s - %s:\n%s", this.username, this.ipAddress, this.canciones);
    }

    @Override
    public int hashCode() {
        return this.username.hashCode();
    }
}
