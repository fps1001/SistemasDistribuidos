package es.ubu.lsi;
import java.rmi.*;

/**
 * Interface remota de los clientes.
 */ 
public interface Cliente extends java.rmi.Remote {
	
   /**
   * Este método es llamado por el servidor remoto para lanzar
   * una ventana popup en el cliente gr�fico.
   * 
   * @param msg mensaje a mostrar
   * @throws RemoteException errores remotos
   */
   void popup(String msg) throws RemoteException;
}
