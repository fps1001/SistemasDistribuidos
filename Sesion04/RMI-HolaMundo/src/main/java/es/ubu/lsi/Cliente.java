package es.ubu.lsi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;

/**
 * Cliente remoto.
 */
public class Cliente {

	/**
	 * Constructor oculto,
	 */
    private Cliente() {}


	/**
	 * Método raíz.
	 *
	 * @param args host con el registro
	 */
    public static void main(String[] args) {

		String host = (args.length < 1) ? null : args[0];
		try {
			// El Registry es un objeto remoto que mapea nombres a objetos remotos.
			// Este paso es esencial para poder registrar o buscar objetos remotos más tarde.
		   Registry registry = LocateRegistry.getRegistry(host);
		   // Resuelve el objeto remoto (la referencia a...)
			//FPS con el método list se obtiene los nombres de los objetos del registro que recorreré en un for
			String[] nombres_objetos = registry.list();
			// Los coloco ya que list los devolvía descolocados.
			Arrays.sort(nombres_objetos);

			// Imprimo la lista de nombres de objetos registrados
			System.out.println("Nombres de objetos registrados en el RMI Registry:");
			for (String nombre : nombres_objetos) {
				System.out.println(nombre);
			}

			for (String name : nombres_objetos) {

				// busca el objeto remoto por su nombre (el que se obtiene en la iteración actual del bucle).
				// El objeto retornado por lookup se hace un casting a HolaMundo, que es la interfaz que define el método decirHola().
				// Este casting es necesario porque lookup devuelve un tipo Remote, que es la superinterfaz de todos los objetos remotos,
				// y debe ser convertido al tipo correcto antes de usarlo.
				HolaMundo stub = (HolaMundo) registry.lookup(name);
				String respuesta = stub.decirHola();
				System.out.println("Respuesta del objeto " + name + ": " + respuesta);

			}
// 			CODIGO BASE
//	 	   HolaMundo stub = (HolaMundo) registry.lookup("Hola");
//	 	   String respuesta = stub.decirHola();
//	       System.out.println("Respuesta del servidor remoto: " + respuesta);
		} 
		catch (Exception e) {
	    	System.err.println("Excepción en cliente: " + e.toString());
		} // try
		
    } // main
    
} // Cliente