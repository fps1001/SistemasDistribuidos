package es.ubu.lsi;

import java.rmi.Naming;

/**
 * Cliente del objeto remoto.
 * 
 */
public class Cliente {

	/**
	 * Cliente remoto.
	 */
	public Cliente() {
    	try {
			HolaMundo obj = (HolaMundo) Naming.lookup("rmi://localhost/Servidor");
            
            // invoca
      		String message  = obj.decirHola();
      		
      		// muestra el mensaje
      		System.out.println(message);
      	} 
      	catch (Exception e) {
			System.err.println("Excepción recogida en cliente: " +e.toString());
      }
   } //Cliente
   
}
