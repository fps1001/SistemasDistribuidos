package es.ubu.lsi;
import java.rmi.RemoteException;

/**
 * Interface remota.
 */ 
public interface Servidor extends java.rmi.Remote {
	
	/**	
	 * Interfaz remota.
	 *
	 * @return mensaje
	 * @throws RemoteException errores remotos
	 * @throws Exception errores generales
	 */
	public Mensaje obtenerMensaje() throws RemoteException, Exception;
	
}

