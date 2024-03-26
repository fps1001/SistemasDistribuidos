package es.ubu.lsi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;

/**
 * Implementación del objeto remoto.
 * 
 */
public class Servidor extends UnicastRemoteObject implements HolaMundo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * Inicia el objeto remoto.
	 *
	 * @throws RemoteException errores remotos
	 */
	public Servidor() throws RemoteException{
    	super();//  llama al constructor de la superclase
   	}


	/**
	 * {@inheritDoc}.
	 *
	 * @return {@inheritDoc}
	 * @throws RemoteException {@inheritDoc}
	 */
	public String decirHola() throws RemoteException{
    	return "Hola mundo, el tiempo actual es " + new Date();
    }
}
