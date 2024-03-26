package es.ubu.lsi;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;

/**
 * Implementación del objeto remoto.
 * 
 */
public class Servidor extends UnicastRemoteObject implements Hola {

	/**
	 * Serial id.
	 */
	private static final long serialVersionUID = -4655637764535389652L;

	/**
	 * Inicia el objeto remoto.
	 * 
	 * @throws RemoteException
	 *             errores remotos
	 */
	public Servidor() throws RemoteException {
		super();
	}

	/**
	 * Retorna un saludo.
	 * 
	 * @param ca
	 *            referencia remota al cliente
	 * @return saludo con la fecha actual
	 * @throws RemoteException
	 *             errores remotos
	 */
	public String decirHola(Cliente ca) throws RemoteException {
		// invocación al método del cliente remoto
		ca.popup("Este es un mensaje desde el servidor!");
		return "Hola mundo, la hora actual es" + new Date();
	}

	/**
	 * Método raíz.
	 * 
	 * @param args
	 *            argumentos
	 */
	public static void main(String args[]) {
		try {
			System.setSecurityManager(new SecurityManager());
			
			// Instancia el objeto remoto, no exportamos
			Servidor obj = new Servidor();
			System.out.println("Objecto instanciado" + obj);

			// Ligado del objeto remoto en el registro
			Naming.rebind("/Servidor", obj);
			System.out.println("Servidor registrado...");
		} catch (Exception e) {
			System.err.println("Excepción recogida: " + e.toString());
		} // try
	} // main

}
