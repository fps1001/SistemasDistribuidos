package es.ubu.lsi;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Implementación del objeto remoto.
 * 
 */
public class ServidorImpl extends UnicastRemoteObject implements Servidor {

	/**
	 * Serial id.
	 */
	private static final long serialVersionUID = -992503786955790038L;

	/**
	 * Constructor objeto remoto.
	 * 
	 * @throws RemoteException
	 *             errores remotos
	 */
	public ServidorImpl() throws RemoteException {
		super();
	}

	/**
	 * Retorna un mensaje como referencia a objeto remoto.
	 * 
	 * @return referencia remota a mensaje
	 * @throws RemoteException
	 *             errores remotos
	 * @throws Exception
	 *             errores generales
	 */
	public Mensaje obtenerMensaje() throws RemoteException, Exception {
		return new ServidorMensaje();
	}

	/**
	 * Método raíz.
	 * 
	 * @param args
	 *            argumentos
	 */
	public static void main(String args[]) {
		try {
			// Instancia el objeto
			// dado que hereda de UnicastRemoteObject no es necesario exportarlo
			ServidorImpl obj = new ServidorImpl();
			System.out.println("Objecto instanciado: " + obj);

			// Ligado del objeto remoto en el registro
			Naming.rebind("/Servidor", obj);
			System.out.println("Servidor registrado...");
		} catch (Exception e) {
			System.err.println("Excepción recogida: " + e.toString());
		}
	} // main
}
