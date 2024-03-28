package es.ubu.lsi.client;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import es.ubu.lsi.server.ChatServer;
import es.ubu.lsi.common.ChatMessage;
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
 */
public class ChatClientStarter {

    private String nickname; // Asigno nombre Anónimo a cliente si no indica nombre.
    private String host; // localhost valor por defecto.

    /**
     * Constructor de la clase.
     *
     * @param args nombre de usuario y host (localhost por defecto)
     */
    public ChatClientStarter(String[] args){
        this.nickname = args.length > 0 ? args[0] : "Anónimo";
        this.host = args.length > 1 ? args[1] : "localhost";
        start(); // Iniciamos el host del cliente
    }

    /**
     * Inicia el chat y los mensajes entre clientes a través del servidor.
     */
    public void start() {
        try {
            //Crea una instancia del cliente del chat con el nickname
            ChatClientImpl chatClient = new ChatClientImpl(nickname, null);

            //Exporta el objeto para hacerlo disponible para recibir llamadas remotas.
            UnicastRemoteObject.exportObject(chatClient, 0);

            //Busca el servidor de chat en el registro RMI usando su nombre conocido.
            Registry registry = LocateRegistry.getRegistry(host);
            ChatServer servidor = (ChatServer) registry.lookup("ChatServer");

            // Ponemos el mensaje como en sesión 3 con el id del usuario.
            int clientId = servidor.checkIn(chatClient);
            System.out.println("Registrado en el servidor con ID: " + clientId);

            //Bucle de mensajes del cliente hasta logout.
            Scanner scanner = new Scanner(System.in);
            String input;
            System.out.println("Escribe tus mensajes (escribe 'logout' para salir):");
            while (!(input = scanner.nextLine()).equalsIgnoreCase("logout")) {
                chatClient.sendMessage(input); // Suponiendo que agregaste un método sendMessage en ChatClientImpl que haga este trabajo.
            }

            servidor.logout(chatClient);
            System.out.println("Desconectado del servidor.");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ChatClientStarter(args);
    }
}
