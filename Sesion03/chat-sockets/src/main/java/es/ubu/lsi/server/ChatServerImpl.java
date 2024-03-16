package es.ubu.lsi.server;

import es.ubu.lsi.common.ChatMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.*;
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
    private int clientId = 1;
    private SimpleDateFormat sdf;
    Map<String, ServerThreadForClient> clients = new ConcurrentHashMap<>();
    // Para no modificar el archivo de ChatMessage tengo que llevar otro mapa con id-usuario.
    // Si fuese mi proyecto desde cero cambiaría la clase a username como clave.
    Map<Integer, String> idToUsername = new ConcurrentHashMap<>();
    // El siguiente mapa será usuario- usuarios bloqueados y gestionará los bans de cada usuario.
    Map<String, Set<String>> userBlocks = new ConcurrentHashMap<>();
    ServerSocket serverSocket;

    public void addUserBlock(String blocker, String blocked) {
        userBlocks.computeIfAbsent(blocker, k -> new HashSet<>()).add(blocked);
    }

    public void removeUserBlock(String blocker, String blocked) {

        if(userBlocks.containsKey(blocker)) {
            userBlocks.get(blocker).remove(blocked);
        }
    }

    public Set<String> getBlockedUsers(String blocker) {
        return userBlocks.getOrDefault(blocker, Collections.emptySet());
    }
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
        // Averiguamos id del remitente
        String senderUsername = idToUsername.get(msg.getId());

        // Creamos el mensaje con el formato "username: message"
        String formattedMessage = senderUsername + ": " + msg.getMessage();
        ChatMessage formattedChatMessage = new ChatMessage(msg.getId(), msg.getType(), formattedMessage);

        // Recorrer todos los clientes y enviarles el mensaje formateado,
        // excepto al cliente que envió el mensaje original y aquellos que lo han bloqueado.

        for (Map.Entry<String, ServerThreadForClient> clientEntry : clients.entrySet()) {
            String recipientUsername = clientEntry.getKey();
            // Comprobar si el destinatario ha bloqueado al emisor con función privada
            if (!recipientUsername.equals(senderUsername) && !hasUserBlocked(recipientUsername, senderUsername)) {
                try {
                    clientEntry.getValue().getOut().writeObject(formattedChatMessage);
                } catch (IOException e) {
                    System.err.println("Error al enviar mensaje a " + recipientUsername);
                    // Considera eliminar al cliente si no se puede enviar el mensaje
                }
            }
        }
//        for (Map.Entry<String, ServerThreadForClient> clientEntry : clients.entrySet()) {
//            if (!clientEntry.getKey().equals(senderUsername)) { // No retransmitir al emisor
//                try {
//                    clientEntry.getValue().getOut().writeObject(formattedChatMessage);
//                } catch (IOException e) {
//                    System.err.println("Error al enviar mensaje a " + clientEntry.getKey());
//                    // Considera eliminar al cliente si no se puede enviar el mensaje
//                }
//            }
//        }
    }
    /**
     * Comprueba si un usuario ha bloqueado a otro.
     *
     * @param recipientUsername El nombre de usuario del destinatario que podría haber bloqueado al emisor.
     * @param senderUsername El nombre de usuario del emisor del mensaje.
     * @return true si el destinatario ha bloqueado al emisor; false en caso contrario.
     */
    private boolean hasUserBlocked(String recipientUsername, String senderUsername) {
        //getOrDefault devolverá el valor vacío si no está el usuario entre los bloqueados del cliente a comprobar.
        Set<String> blockedUsers = userBlocks.getOrDefault(recipientUsername, Collections.emptySet());
        // Si devuelve el usuario entonces es que está bloqueado y devolverá true.
        return blockedUsers.contains(senderUsername);
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
                    String[] parts = message.getMessage().split("\\s+", 2); // Separa el comando del resto del mensaje
                    // Si el tipo es ban/unban o mensaje normal:
                    if (message.getType() == ChatMessage.MessageType.MESSAGE) {
                        if (parts[0].equalsIgnoreCase("ban") && parts.length > 1) {
                            String userToBan = parts[1];
                            server.addUserBlock(this.username, userToBan); // Agrega el bloqueo
                            //TODO
                            //server.saveConfig(server.getConfigFile()); // Guarda la nueva configuración
                        } else if (parts[0].equalsIgnoreCase("unban") && parts.length > 1) {
                            String userToUnban = parts[1];
                            server.removeUserBlock(this.username, userToUnban); // Elimina el bloqueo
                            //TODO
                            //server.saveConfig(server.getConfigFile()); // Guarda la nueva configuración
                        }else {
                            // Trata el mensaje como un mensaje de chat regular
                            System.out.println(this.username + ": " + message.getMessage());
                            server.broadcast(message); // Retransmite el mensaje
                        }
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



