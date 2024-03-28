package es.ubu.lsi.client;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import es.ubu.lsi.server.ChatServer;
import java.util.Scanner;

/**
 * ChatClientStarter: Inicia la comunicación con el servidor.
 * Sería el que haga las funciones de Cliente de la sesión 4.
 *
 * <p>
 * Para más información sobre el proyecto, consultar el repositorio de GitHub.
 * Contacto: fps1001@alu.ubu.es
 *
 * @see <a href="https://github.com/fps1001/SistemasDistribuidos">Repositorio de GitHub</a>
 * @author Fernando Pisot Serrano
 */

/**
 * Constructor de la clase.
 *
 * @param args nombre de usuario y host (localhost por defecto)
 */
public class ChatClientStarter (String[] args){

    String nickname = args.length > 0 ? args[0] : "Anónimo"; // Asigno nombre Anónimo a cliente si no indica nombre.
    String host = args.length > 1 ? args[1] : "localhost"; // localhost valor por defecto.
    start(); // Iniciamos el host del cliente

    /**
     * Inicia el chat y los mensajes entre clientes a través del servidor.
     */
    public void start() {
        try {

            //* Crea instancia de ChatClientImpl y se registra en el servidor
            UnicastRemoteObject.exportObject(chatClient, 0);
            // registry paso esencial para poder buscar objetos remotos más tarde.
            Registry registry = LocateRegistry.getRegistry(hostCliente);
            // busca el objeto remoto por su nombre
            ChatServer servidor = (ChatServer) registry.lookup("/servidor");


            //* Confirmación de registro como en el caso de sockets
            int clientId = server.checkIn(client);
            System.out.println("Registrado en el servidor con ID: " + clientId);

            //* Manejo de entradas del usuario
            Scanner scanner = new Scanner(System.in);
            String input;
            System.out.println("Escribe tus mensajes (escribe 'logout' para salir):");
            while (!(input = scanner.nextLine()).equalsIgnoreCase("logout")) {

                //* Envia mensaje al servidor
                server.publish(new ChatMessage(clientId, nickname, input));
            }

            // Logout
            server.logout(client);
            System.out.println("Desconectado del servidor.");

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
