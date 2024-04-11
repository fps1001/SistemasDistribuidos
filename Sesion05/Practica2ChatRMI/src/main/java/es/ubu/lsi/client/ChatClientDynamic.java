package es.ubu.lsi.client;

import java.lang.reflect.Constructor;
import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClassLoader;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;
import java.util.Scanner;

/**
 * Dynamic client: maneja la configuración dinámica del cliente, cargando la clase ChatClientStarter para iniciar
 * la ejecución del cliente.
 * 
 * @author Raúl Marticorena
 * @author Joaquin P. Seco
 */
public class ChatClientDynamic {

	/**
	 * Método raíz.
	 * 
	 * @param args
	 *            parámetros
	 */
	public static void main(String[] args) {

		try {
			if (System.getSecurityManager() == null) {
				System.setSecurityManager(new RMISecurityManager());
			}
			Properties p = System.getProperties();
			// Reads codebase
			String url = p.getProperty("java.rmi.server.codebase");
			// Load class
			Class<?> clientClass;
			clientClass = RMIClassLoader.loadClass(url,
					"es.ubu.lsi.client.ChatClientDynamic.ChatClientStarter");
			// Starts client
			Constructor<?> cons = clientClass.getConstructor(String[].class);			
			cons.newInstance((Object)args);
		} catch (Exception e) {
			System.err.println("Excepcion en arranque del cliente " + e.toString());
		}
	}

    /**
     * ChatClientStarter: Inicia la comunicación con el servidor.
     * Sería el que haga las funciones de Cliente de la sesión 4.
     *
     * <p>
     * Para más información sobre el proyecto, consultar el repositorio de GitHub.
     * Contacto: fps1001@alu.ubu.es
     *
     * @see <a href="https://github.com/fps1001/SistemasDistribuidos">Repositorio de GitHub</a>
     */


        public static class ChatClientStarter {

            public static void main(String[] args) {
                try {
                    String nickname = args[0];
                    String host = args.length < 2 ? "localhost" : args[1];
                    // El Registry es un objeto remoto que mapea nombres a objetos remotos.
                    // Este paso es esencial para poder registrar o buscar objetos remotos más tarde.
                    Registry registry = LocateRegistry.getRegistry(host);
                    es.ubu.lsi.server.ChatServer server = (es.ubu.lsi.server.ChatServer) registry.lookup("/servidor");

                    ChatClientImpl client = new ChatClientImpl(nickname, server);
                    // Exportación del cliente remoto:
                    UnicastRemoteObject.exportObject(client,0);
                    // Enlaza ambos objetos: ChatClientImpl y ChatServer: cliente y servidor.
                    server.checkIn(client);

                    System.out.println("Cliente iniciado. Escriba sus mensajes:");
                    try (Scanner scanner = new Scanner(System.in)) {
                        while (true) {
                            // TODO El primer mensaje debería ser el usuario?
                            String message = scanner.nextLine();
                            if ("logout".equalsIgnoreCase(message)) {
                                server.logout(client); //TODO Debería cerrar el scanner
                                break;
                            } else {
                                client.sendMessage(message); // montará un tipo ChatMessage y hará server.publish
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Excepción del cliente: " + e.toString());
                    e.printStackTrace();
                }
            }
        }
}
