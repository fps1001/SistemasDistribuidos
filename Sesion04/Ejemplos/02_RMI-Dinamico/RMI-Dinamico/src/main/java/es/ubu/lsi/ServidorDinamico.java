package es.ubu.lsi;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.server.RMIClassLoader;
import java.util.Properties;


/**
 * Cargador dinámico del servidor.
 * Registro del objeto remoto en el registro (servicio de nombres) de Java RMI.
 * Es necesario lanzar previamente rmiregistry.
 */ 
public class ServidorDinamico {
	/**
	 * Método raíz.
	 * 
	 * @param args argumentos 
	 */
   	public static void main(String args[]) {

      // Crea e instala el gestor de seguridad
      if (System.getSecurityManager() == null) {
        System.setSecurityManager(new SecurityManager());
      }
      try {
        Properties p = System.getProperties();
        // lee el goodbyes
        String url = p.getProperty("java.rmi.server.codebase");
        // Cargador de clases dinámico ...
        Class<?> serverclass = RMIClassLoader.loadClass(url, "es.ubu.lsi.Servidor");
        Naming.rebind("/Servidor", (Remote) serverclass.newInstance());
        System.out.println("Servidor registrado...");
      } 
      catch (Exception e) {
		System.err.println("Excepcion recogida: " + e.toString());
      }
   }
   
} // ServidorDinamico
