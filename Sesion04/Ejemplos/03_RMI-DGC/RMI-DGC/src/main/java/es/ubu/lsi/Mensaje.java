package es.ubu.lsi;
import java.rmi.RemoteException;

/**
 * Interface remota.
 */ 
public interface Mensaje extends java.rmi.Remote{
	
	/**
	 * Devuelve un mensaje.
	 *
	 * @return texto
	 * @throws RemoteException error remoto
	 */ 
	public String estarVivo() throws RemoteException;
	
}

