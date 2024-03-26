package es.ubu.lsi;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.server.Unreferenced;

/**
 * Mensajes remotos sobre los que se hace el leasing.
 * 
 */
public class ServidorMensaje extends UnicastRemoteObject implements Mensaje,
		Serializable, Unreferenced {

	/**
	 * Serial id.
	 */
	private static final long serialVersionUID = -3689575156115984888L;

	/**
	 * Contador para el número de instancias de la clase.
	 */
	private static int contador;

	/**
	 * Identificador para la instancia
	 */
	private int id;

	/**
	 * Instancia mensaje remoto.
	 * 
	 * @throws RemoteException
	 *             errores remotos
	 */
	public ServidorMensaje() throws RemoteException {
		super();
		System.out.println("Creado mensaje:" + contador);
		contador++;
		setId(contador);
	}

	/**
	 * Devuelve un mensaje si el objeto está vivo.
	 * 
	 * @return texto identificando el objeto vivo
	 * @throws RemoteException
	 *             error remoto
	 */
	public String estarVivo() throws RemoteException {
		return "El objeto " + id + " está vivo";
	} // estaVivo

	/**
	 * Elimina de memoria.
	 * 
	 */
	@Override
	public void finalize() throws Throwable {
		super.finalize();
		System.out.println("Finalizada llamada para mensaje: " + id);
	} // finalize

	/**
	 * Unreferenced.
	 */
	public void unreferenced() {
		System.out.println("El metodo \"unreferenced()\" llamado por Msg: "
				+ id);
		// Si se necesita, se puede llamar a unexportObject si nadie lo usa
		// unexportObject(this, true);
	} // unreferenced

	/**
	 * Establece el valor del identificador.
	 * 
	 * @param id
	 *            identificador de objeto
	 */
	private void setId(int id) {
		this.id = id;
	} // setId

} // ServidorMensaje
