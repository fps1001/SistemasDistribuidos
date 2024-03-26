package es.ubu.lsi;

import java.rmi.Naming;


/**
 * Cliente del objeto remoto.
 * 
 */
public class Cliente {

	/**
	 * Método raíz.
	 * 
	 * @param args
	 *            argumentos
	 */
	public static void main(String args[]) {
		final int NUM = 100;

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		try {

			Servidor obj;
			String host;
			// búsqueda y resolución del objeto remoto principal por nombre

			if (args.length == 0) {
				host = "localhost"; // por defecto
			} else {
				host = args[0];
			}

			// Resolviendo...
			System.out.println("Resolviendo en: " + host);
			obj = (Servidor) Naming.lookup("//" + host + "/Servidor");

			// reserva de mensajes
			Mensaje msg[] = new Mensaje[NUM];

			for (int i = 0; i < NUM; i++) {
				// llamada al método remoto y obtención de referencias remotas a
				// mensajes
				msg[i] = obj.obtenerMensaje();
				System.out.println(msg[i].estarVivo());
			}

			// DESCOMENTAR EL CÓDIGO Y VER SU EFECTO
//			for (int i = 0; i < NUM; i+=3) {
//				System.out.println(msg[i].estarVivo() + "---> Anulado");
//				msg[i] = null;
//				System.gc(); // Forzando un gc
//			}
			
			System.out.println("Esperando...");
			Thread.sleep(30000);

		} catch (Exception e) {
			System.err
					.println("Excepción recogida en cliente: " + e.toString());
		}
	} // main
} // Cliente

