package es.ubu.lsi.server;

import es.ubu.lsi.common.ChatMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Map;


/**
 * Implementación principal del servidor de chat.
 * Gestiona las conexiones entrantes y la creación de hilos para cada cliente.
 * Por defecto, el servidor escucha en el puerto 1500.
 * <p>
 * Ejemplo de uso: java es.ubu.lsi.server.ChatServerImpl
 *
 * @author Fernando Pisot Serrano
 * @email fps1001@alu.ubu.es
 * @repo <a href="https://github.com/fps1001/SistemasDistribuidos">...</a>
 *
 * <a href="https://socket.io/docs/v3/broadcasting-events">...</a>
 */

public class ChatServerImpl implements ChatServer {
    private static final int DEFAULT_PORT = 1500;
    private boolean alive = true;
    //Añado clases a partir del multihilo.
    private int clientId;
    private SimpleDateFormat sdf;
    Map<String, ServerThreadForClient> clients;
    ServerSocket serverSocket;


    public static void main(String[] args){

        if (args.length != 0) {
            System.err.println("Usage: java es.ubu.lsi.server.ChatServerImpl ");
            System.exit(1);
        }

        System.out.println("Escuchando por puerto: " + DEFAULT_PORT);


        // Creamos un servidor de la clase de este archivo java y ejecutamos startup.
        ChatServerImpl server = new ChatServerImpl();
        server.startup();



    }

    @Override
    public void startup() {
        try{
            ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT);
            while (alive){
                Socket clientSocket = serverSocket.accept();
                // Si pasa el corte establece la conexión a un nuevo hilo.
                System.out.println("Nuevo Cliente: " + clientSocket.getInputStream());
                Thread hilonuevocliente = new ServerThreadForClient(clientSocket);
                hilonuevocliente.start();
            }

        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port " + DEFAULT_PORT + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void broadcast(String msg) {
        //TODO enviará mensaje a todos (Todos menos al que lo ha mandado!) para lo que recorrerá el map
    }

    @Override
    public void remove(int id) {
        //TODO enviará mensaje a todos de que está eliminado el usuario con id=id
        //Se borrará del Map
        //Desconectará al usuario con id=id.
    }

    @Override
    public void shutdown() {
        try {
                if (this.serverSocket != null && !this.serverSocket.isClosed()) {
                    serverSocket.close(); // Cierra el socket del cliente.
                }
                System.out.println("Conexión cerrada para el cliente ID: " + this.clientId);
            } catch (IOException e) {
                System.err.println("Error al cerrar la conexión para el cliente ID: " + this.clientId + ": " + e.getMessage());
            } finally {
                alive = false; // Asegura que el hilo del bucle se detenga.
            }
        }
    }


class ServerThreadForClient extends Thread {
    private Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private int clientId;
    private ChatServerImpl server; // Referencia al servidor para poder llamar a métodos como broadcast

    public ServerThreadForClient(Socket clientSocket, int clientId, ChatServerImpl server) {
        this.clientSocket = clientSocket;
        this.clientId = clientId;
        this.server = server;
        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            System.out.println("Error al crear flujos de entrada/salida: " + e.getMessage());
        }
    }

    public void run() {
        // Cambio el printer del multihilo por un objeto serializable
        try {
            Object inputObject;

            while ((inputObject = in.readObject()) != null) {
                if (inputObject instanceof ChatMessage) {
                    ChatMessage message = (ChatMessage) inputObject;
                    System.out.println("Mensaje de cliente " + clientId + ": " + message.getMessage());
                    // Aquí puedes implementar la lógica para manejar diferentes tipos de mensajes
                    // Por ejemplo, retransmitir el mensaje a otros clientes
                    server.broadcast(message);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Exception caught on thread for client " + clientId + ": " + e.getMessage());
        } finally {
            // Asegurarse de cerrar recursos y notificar al servidor que el cliente se desconectó
            server.remove(clientId);
            try {
                if (out != null) out.close();
                if (in != null) in.close();
                if (clientSocket != null) clientSocket.close();
            } catch (IOException e) {
                System.out.println("Error al cerrar flujos o socket para el cliente " + clientId);
            }
        }
    }
}