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
* */

    public class ChatClientStarter {
        /** Nombre de identificación del cliente */
        private String nickname;
        /** Nombre del host al que se va a conectar*/
        private String host;

        /**
         * ChatClientStarter. Constructor de clase.
         * */
        public ChatClientStarter(String[] args) {
            this.nickname = args[0];
            this.host = "localhost";
            start();
        }

    public void start() {
            try {
                // El Registry es un objeto remoto que mapea nombres a objetos remotos.
                // Este paso es esencial para poder registrar o buscar objetos remotos más tarde.
                Registry registry = LocateRegistry.getRegistry(host);
                ChatServer server = (ChatServer) registry.lookup("/servidor");

                ChatClientImpl client = new ChatClientImpl(nickname, server);
                // Exportación del cliente remoto:
                UnicastRemoteObject.exportObject(client,0);
                // Enlaza ambos objetos: ChatClientImpl y ChatServer: cliente y servidor.
                server.checkIn(client);

                System.out.println("Cliente iniciado. Escriba sus mensajes:");
                try (Scanner scanner = new Scanner(System.in)) {
                    while (true) {
                        String message = scanner.nextLine();
                        if ("logout".equalsIgnoreCase(message)) {
                            client.disconnect(); // Limpieza de la conexión RMI.
                            System.out.println("Saliendo de la sala de chat... Adios");
                            break;
                        } else if (message.toLowerCase().startsWith("drop ")) {
                            String userToDrop = message.substring(5);
                            server.dropUser(client, userToDrop); // Le pasa el emisor para recibir contestación de servidor.
                        }else {
                            try { // Hago este estado para evitar que un dropeado envíe mensajes y salga más ordenadamente.
                                client.sendMessage(message); // -> server.publish (msg)
                            }catch (IllegalStateException e){
                                System.out.println(e.getMessage());
                                break;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Excepción del cliente: " + e.toString());
                e.printStackTrace();
            }
        }
    }


