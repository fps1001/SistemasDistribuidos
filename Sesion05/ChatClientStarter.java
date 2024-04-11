package es.ubu.lsi.client;

import es.ubu.lsi.server.ChatServer;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
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

        public static void main(String[] args) {
            try {
                String nickname = args[0];
                String host = args.length < 2 ? "localhost" : args[1];
                // El Registry es un objeto remoto que mapea nombres a objetos remotos.
                // Este paso es esencial para poder registrar o buscar objetos remotos más tarde.
                Registry registry = LocateRegistry.getRegistry(host);
                ChatServer server = (ChatServer) registry.lookup("/servidor");

                es.ubu.lsi.client.ChatClientImpl client = new es.ubu.lsi.client.ChatClientImpl(nickname, server);
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


