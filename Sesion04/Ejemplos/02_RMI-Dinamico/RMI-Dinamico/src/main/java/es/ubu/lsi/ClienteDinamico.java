package es.ubu.lsi;

import java.rmi.server.RMIClassLoader;
import java.util.Properties;

/**
 * Cliente del objeto remoto.
 * 
 */
public class ClienteDinamico {

	/**
	 * Cargardor dinámico del cliente.
	 * Inicializa la configuración para la carga dinámica.
	 *
	 * @throws Exception error general
	 */
   	public ClienteDinamico() throws Exception {
      Properties  p = System.getProperties();
      // lee el codebase
      String url = p.getProperty("java.rmi.server.codebase");
      // cargador de clases
      Class<?> clientClass = RMIClassLoader.loadClass(url, "es.ubu.lsi.Cliente");
      // inicia el cliente
      clientClass.newInstance();
   	}

	/**
	 * M�todo raíz.
	 *
	 * @param args parámetros
	 */
   	public static void main (String args[]) {
   		// gestor de seguridad
    	System.setSecurityManager(new SecurityManager());
    	try {
    		// genera un cliente
        	ClienteDinamico dc = new ClienteDinamico();
    	} 
    	catch (Exception e) {
        	System.err.println(e.toString());
      	}
   	}
}
