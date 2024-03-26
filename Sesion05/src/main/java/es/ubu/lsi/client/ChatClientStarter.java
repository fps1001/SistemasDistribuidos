package es.ubu.lsi.client;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import es.ubu.lsi.server.ChatServer;
import java.util.Scanner;

/**
 * ChatClientStarter: Inicia la comunicación con el servidor.
 *
 * <p>
 * Para más información sobre el proyecto, consultar el repositorio de GitHub.
 * Contacto: fps1001@alu.ubu.es
 *
 * @see <a href="https://github.com/fps1001/SistemasDistribuidos">Repositorio de GitHub</a>
 * @author Fernando Pisot Serrano
 */

public class ChatClientStarter {

    // TODO Quizá haya que ponerlo en un start por ser un hilo...
    public static void main(String[] args) {
        try {
            String nickname = args.length > 0 ? args[0] : "Anónimo"; // Asigno nombre Anónimo a cliente si no indica nombre.
            String host = args.length > 1 ? args[1] : "localhost"; // localhost valor por defecto.

            // Exportación y vinculación del cliente remoto
            System.setProperty("java.security.policy", "path/to/your/security/policy");
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }

            // Crear instancia de ChatClientImpl y registrarse en el servidor
            ChatClientImpl client = new ChatClientImpl(host, nickname);
            ChatServer server = (ChatServer) Naming.lookup("//" + host + "/ChatServer");
            int clientId = server.checkIn(client);

            // Confirmación de registro como en el caso de sockets
            System.out.println("Registrado en el servidor con ID: " + clientId);

            // Manejo de entradas del usuario
            Scanner scanner = new Scanner(System.in);
            String input;
            System.out.println("Escribe tus mensajes (escribe 'logout' para salir):");
            while (!(input = scanner.nextLine()).equalsIgnoreCase("logout")) {
                // Enviar mensaje al servidor
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
