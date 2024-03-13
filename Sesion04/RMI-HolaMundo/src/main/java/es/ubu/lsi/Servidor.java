package es.ubu.lsi;
	
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Servidor remoto.
 *
 */	
public class Servidor implements HolaMundo {
	
	/**
	 * {@inheritDoc}.
	 *
	 * @return {@inheritDoc}
	 */
    public String decirHola() {
		return "Hola mundo!";
    }
	
	/**
	 * Método raíz.
	 *
	 * @param args argumentos
	 */
    public static void main(String args[]) {
	
		try {
		    Servidor objeto1 = new Servidor();
			//Añado 2 objetos más
			Servidor objeto2 = new Servidor();
			Servidor objeto3 = new Servidor();
		    
		    // si no hereda de UnicastRemoteObject es necesario exportar
	    	//HolaMundo stub = (HolaMundo) UnicastRemoteObject.exportObject(obj, 0);
			//FPS Exportamos 3 objetos.
			// https://www.geeksforgeeks.org/difference-between-rmi-and-socket/ 🡳🡳🡳🡳🡳
			// Los stubs son la puerta de entrada para el lado del cliente, construyen un bloque de información y
			// envían esta información al lado del servidor.
			// El bloque de información puede consistir en el nombre del método que se va a invocar,
			// los parámetros para la JVM remota y el identificador del objeto remoto a utilizar.
			HolaMundo stub1 = (HolaMundo) UnicastRemoteObject.exportObject(objeto1, 0);
			HolaMundo stub2 = (HolaMundo) UnicastRemoteObject.exportObject(objeto2, 0);
			HolaMundo stub3 = (HolaMundo) UnicastRemoteObject.exportObject(objeto3, 0);


		    // Liga el resguardo de objeto remoto en el registro
			//FPS Solo un registro pero 3 vinculos de nombre a un objeto remoto en el registro RMI
			// Se utiliza para registrar objetos remotos en el RMI Registry, permitiendo que los clientes localicen
			// estos objetos remotos por nombre y los invoquen como si estuvieran localmente disponibles.
	    	Registry registro = LocateRegistry.getRegistry();
	    	registro.bind("Hola", stub1);
			registro.bind("Hola2", stub2);
			registro.bind("Hola3", stub3);

	
	    	//System.out.println("Servidor preparado");
			System.out.println("Servidor preparado y tres objetos registrados.");
		}
		catch (Exception e) {
		    System.err.println("Excepción de servidor: " + e.toString());
		}
    } // main
    
} // Servidor