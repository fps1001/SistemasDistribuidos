package es.ubu.lsi.server;

import es.ubu.lsi.common.ChatMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


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

class ChatServerImpl implements ChatServer {
    private static final int DEFAULT_PORT = 1500;
    private boolean alive = true;
    //Añado clases a partir del multihilo.
    private static int clientId = 1;
    private SimpleDateFormat sdf;
    Map<String, ServerThreadForClient> clients = new ConcurrentHashMap<>();
    // Para no modificar el archivo de ChatMessage tengo que llevar otro mapa con id-usuario.
    // Si fuese mi proyecto desde cero cambiaría la clase a username como clave.
    Map<Integer, String> idToUsername = new ConcurrentHashMap<>();

    ServerSocket serverSocket;

    public void registerClient(int clientId, String username, ServerThreadForClient clientThread) {
        clients.put(username, clientThread);
        idToUsername.put(clientId, username); // Almacena la asociación de ID a username
        // System.out.println("DEBUG: " + username + " con id: " + clientId + " ha sido registrado.");
    }


    public void removeClient(String username) {
        // Encuentra el ID correspondiente al username y lo elimina de idToUsername
        Integer clientIdToRemove = null;
        for (Map.Entry<Integer, String> entry : idToUsername.entrySet()) {
            if (entry.getValue().equals(username)) {
                clientIdToRemove = entry.getKey();
                break;
            }
        }
        if (clientIdToRemove != null) {
            idToUsername.remove(clientIdToRemove);
        }
        clients.remove(username); // Elimina al cliente del map de clientes
        //System.out.println(username + " ha sido desconectado.");
    }


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
            serverSocket = new ServerSocket(DEFAULT_PORT);
            while (alive){
                Socket clientSocket = serverSocket.accept();
                // Si pasa el corte establece la conexión a un nuevo hilo.
                //System.out.println("Nuevo Cliente: " + clientSocket);
                int thisClientId = clientId++; // Asigna y luego incrementa el valor para el próximo cliente
                ServerThreadForClient clientThread = new ServerThreadForClient(clientSocket, this, thisClientId);

                clientThread.start();
            }

        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port " + DEFAULT_PORT + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }


    @Override
    public void broadcast(ChatMessage msg) {
//        // Extraer el nombre de usuario del cliente que envió el mensaje original.
//        System.out.println("DEBUG: Mapa de idToUsername:");
//        for(Map.Entry<Integer, String> entry : idToUsername.entrySet()) {
//            System.out.println("ID: " + entry.getKey() + " -> Username: " + entry.getValue());
//        }
//
//        System.out.println("DEBUG: Mapa de clients:");
//        for(Map.Entry<String, ServerThreadForClient> entry : clients.entrySet()) {
//            System.out.println("Username: " + entry.getKey() + " -> ClientThread: " + entry.getValue());
//        }

        String senderUsername = idToUsername.get(msg.getId());


        //System.out.println("DEBUG: Nombre de usuario asignado - " + senderUsername + ". id: " + msg.getId());



        // Crear el mensaje con el formato "username: message"
        String formattedMessage = senderUsername + ": " + msg.getMessage();
        ChatMessage formattedChatMessage = new ChatMessage(msg.getId(), msg.getType(), formattedMessage);

        // Recorrer todos los clientes y enviarles el mensaje formateado,
        // excepto al cliente que envió el mensaje original.
        for (Map.Entry<String, ServerThreadForClient> clientEntry : clients.entrySet()) {
            if (!clientEntry.getKey().equals(senderUsername)) { // No retransmitir al emisor
                try {
                    clientEntry.getValue().getOut().writeObject(formattedChatMessage);
                } catch (IOException e) {
                    System.err.println("Error al enviar mensaje a " + clientEntry.getKey());
                    // Considera eliminar al cliente si no se puede enviar el mensaje
                }
            }
        }
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
    private String username;
    private int clientId;
    private ChatServerImpl server; // Referencia al servidor para poder llamar a métodos como broadcast

    public ServerThreadForClient(Socket clientSocket, ChatServerImpl server, int clienteId) {
        this.clientSocket = clientSocket;
        this.server = server;
        this.clientId = clienteId;

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
            // Enviar al cliente su id asignado inmediatamente después de la conexión.
            out.writeObject(new ChatMessage(clientId, ChatMessage.MessageType.MESSAGE, "Tu id es: " + clientId));
            // Espera el primer mensaje, que consideramos el nombre de usuario.
            Object inputObject = in.readObject();
            if (inputObject instanceof ChatMessage) {
                ChatMessage initMessage = (ChatMessage) inputObject;
                this.username = initMessage.getMessage(); // Suponemos que el mensaje contiene el nombre de usuario

                //System.out.println("DEBUG: Nombre de usuario asignado - " + this.username);

                server.registerClient(this.clientId,this.username, this); // Método adicional en ChatServerImpl para añadir el cliente

                System.out.println("Cliente " + this.username + " con id: " + this.clientId + " conectado.");

                // Ahora entra en un bucle para leer mensajes siguientes
                while (true) {
                    ChatMessage message = (ChatMessage) in.readObject();
                    if (message.getType() == ChatMessage.MessageType.MESSAGE) {
                        System.out.println(this.username + ": " + message.getMessage());
                        server.broadcast(message); // Solo retransmite mensajes que no sean de logout
                    } else if (message.getType() == ChatMessage.MessageType.LOGOUT) {
                        break; // Rompe el bucle si el mensaje es de tipo LOGOUT
                    }
                }
            }
        } catch (EOFException | SocketException e) {
            // Estas excepciones se esperan al cerrar el socket del cliente
            System.out.println(this.username + " se ha desconectado con un cierre de socket.");
        } catch (IOException e) {
            System.err.println("Error de E/S en el hilo del cliente " + this.username + ": " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Clase no encontrada al leer el objeto en el hilo del cliente " + this.username + ": " + e.getMessage());
        } finally {
            server.removeClient(this.username); // Elimina al cliente del mapa
            closeConnection(); // Cierra la conexión
        }
    }

    private void closeConnection() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (clientSocket != null) clientSocket.close();
            System.out.println("Conexión cerrada para el cliente " + this.username);
        } catch (IOException e) {
            System.err.println("Error al cerrar la conexión para el cliente " + this.username + ": " + e.getMessage());
        }
    }

    // Método para obtener el nombre de usuario asociado con este hilo de cliente
    public String getUsername() {
        return username;
    }

    // Método para obtener el ObjectOutputStream asociado con este hilo de cliente
    public ObjectOutputStream getOut() {
        return out;
    }

}



