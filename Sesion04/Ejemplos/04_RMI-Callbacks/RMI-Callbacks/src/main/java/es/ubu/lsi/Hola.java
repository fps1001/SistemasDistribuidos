package es.ubu.lsi;
import java.rmi.*;
/**
 * Interface remota.
 */ 
public interface Hola extends Remote {
	
	/**
   * Este m�todo es llamado por los clientes remotos y es implementado
   * por los objetos remotos.
   *
   * @param app referencia remota al cliente
   * @return saludo
   * @throws RemoteException errores remotos
   */
   String decirHola(Cliente app) throws RemoteException;
} 
