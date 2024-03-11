package es.ubu.lsi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

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
		   Registry registry = LocateRegistry.getRegistry(host);
		   // Resuelve el objeto remoto (la referencia a...)
			//FPS con el método list se obtiene los nombres de los objetos del registro.
			String[] nombres_objetos = registry.list();
			for (String name : nombres_objetos) {
				HolaMundo stub = (HolaMundo) registry.lookup(name);
				String respuesta = stub.decirHola();
				System.out.println("Respuesta del objeto " + name + ": " + respuesta);
			}

//	 	   HolaMundo stub = (HolaMundo) registry.lookup("Hola");
//	 	   String respuesta = stub.decirHola();
//	       System.out.println("Respuesta del servidor remoto: " + respuesta);
		} 
		catch (Exception e) {
	    	System.err.println("Excepción en cliente: " + e.toString());
		} // try
		
    } // main
    
} // Cliente