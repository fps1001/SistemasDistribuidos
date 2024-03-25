package es.ubu.lsi.server;

import es.ubu.lsi.common.ChatMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Clase que implementa la funcionalidad del servidor para un sistema de chat distribuido.
 * Administra las conexiones entrantes y maneja múltiples clientes mediante hilos.
 * El servidor escucha en el puerto 1500 por defecto.
 * <p>
 * Uso: java es.ubu.lsi.server.ChatServerImpl
 * <p>
 * Para más información sobre eventos de broadcast:
 * @see <a href="https://socket.io/docs/v3/broadcasting-events">Socket.io Broadcasting Events</a>
 * <p>
 * Para más información sobre el proyecto, consultar el repositorio de GitHub:
 * @see <a href="https://github.com/fps1001/SistemasDistribuidos">Repositorio de GitHub</a>
 * Contacto: fps1001@alu.ubu.es
 *
 * @author Fernando Pisot Serrano
 */

class ChatServerImpl implements ChatServer {
    private static final int DEFAULT_PORT = 1500;
    private boolean alive = true;
    //Añado clases a partir del multihilo.
    private int clientId = 1;
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    Map<String, ServerThreadForClient> clients = new ConcurrentHashMap<>();
    // Para no modificar el archivo de ChatMessage tengo que llevar otro mapa con id-usuario.
    // Si fuese mi proyecto desde cero cambiaría la clase a username como clave.
    Map<Integer, String> idToUsername = new ConcurrentHashMap<>();
    // El siguiente mapa será usuario- usuarios bloqueados y gestionará los bans de cada usuario.
    Map<String, Set<String>> userBlocks = new ConcurrentHashMap<>();
    //Archivo configuración y baneos
    private Properties banConfig = new Properties();
    private File configFile = new File("banConfig.properties");
    ServerSocket serverSocket;

    // Constructor que inicializa el archivo de configuración y carga baneos.
    public ChatServerImpl() {
        try {
            // Define la ruta del directorio de configuración
            Path configPath = Paths.get("./chat-sockets/config");
            // Crea el directorio de configuración si no existe
            if (!Files.exists(configPath)) {
                Files.createDirectories(configPath);
            }
            // Inicializa el archivo de configuración en el directorio
            this.configFile = configPath.resolve("banConfig.properties").toFile();
            // Carga la configuración de baneos
            loadBanConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método principal para iniciar el servidor de chat.
     * Crea y arranca una nueva instancia del servidor.
     *
     * @param args Argumentos de línea de comandos (no se utilizan aquí).
     */
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

    /**
     * Inicia el servidor y acepta conexiones de clientes.
     */
    @Override
    public void startup() {
        try {
            initializeServer();
            acceptClientConnections();
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port " + DEFAULT_PORT + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }

    /**
     * Agrega un usuario a la lista de bloqueados de otro usuario.
     * Este método actualiza tanto la estructura de datos en memoria como el archivo de configuración persistente.
     * @param blocker El nombre de usuario que bloquea.
     * @param blocked El nombre de usuario a ser bloqueado.
     */
    public void addUserBlock(String blocker, String blocked) {
        // Actualiza la estructura de datos en memoria
        userBlocks.computeIfAbsent(blocker, k -> new HashSet<>()).add(blocked);
        // Actualiza la configuración de baneos
        updateBanConfig(blocker);
    }

    /**
     * Actualiza el archivo de bloqueos del usuario pasado por parámetro.
     * @param username
     */
    private void updateBanConfig(String username) {
        Set<String> blockedUsers = userBlocks.get(username);
        if (blockedUsers != null && !blockedUsers.isEmpty()) {
            banConfig.setProperty(username, String.join(",", blockedUsers));
        } else {
            banConfig.remove(username); // Si no hay usuarios bloqueados, elimina la clave
        }
        saveBanConfig();
    }

    /**
     * Realizará un unban actualizando el archivo de configuración.
     * @param blocker
     * @param blocked
     */
    public void removeUserBlock(String blocker, String blocked) {
        if (userBlocks.containsKey(blocker)) {
            userBlocks.get(blocker).remove(blocked);
            updateBanConfig(blocker);
        }
    }

    /**
     * Guarda los datos del cliente una vez establecida la comunicación: id, nombre e hilo en sus respectivos mapas.
     * @param clientId
     * @param username
     * @param clientThread
     */
    public void registerClient(int clientId, String username, ServerThreadForClient clientThread) {
        String timestamp = sdf.format(new Date());
        System.out.println(timestamp + " - Cliente " + username + " con id: " + clientId + " ha sido registrado.");
        clients.put(username, clientThread);
        idToUsername.put(clientId, username); // Almacena la asociación de ID a username
        // System.out.println("DEBUG: " + username + " con id: " + clientId + " ha sido registrado.");
    }

    /**
     * Borra los datos de un usuario en los mapas de las variables: id, nombre e hilo.
     * @param username
     */
    public void removeClient(String username) {

    }

    /**
     * Inicializa el servidor, creando un nuevo ServerSocket y cargando la configuración de baneos.
     * @throws IOException Si ocurre un error al crear el ServerSocket o cargar configuraciones.
     */
    private void initializeServer() throws IOException {
        loadBanConfig();
        serverSocket = new ServerSocket(DEFAULT_PORT);
    }

    /**
     * Acepta conexiones entrantes de clientes y las maneja en hilos separados.
     * @throws IOException Si ocurre un error al aceptar conexiones de clientes.
     */
    private void acceptClientConnections() throws IOException {
        while (alive) {
            Socket clientSocket = serverSocket.accept();
            //Si pasa este punto hay un nuevo cliente y hay que manejar la conexión.
            handleNewClientConnection(clientSocket);
        }
    }

    /**
     * Maneja una nueva conexión de cliente, iniciando un hilo para este.
     * @param clientSocket El socket del cliente conectado.
     */
    private void handleNewClientConnection(Socket clientSocket) {
        int thisClientId = clientId++;
        //Crea un nuevo hilo con los datos de id y la clase del padre (servidor)
        ServerThreadForClient clientThread = new ServerThreadForClient(clientSocket, this, thisClientId, this.sdf);
        clientThread.start();
    }


    /**
     * Carga la configuración de baneos desde un archivo.
     * Si el archivo no existe, se crea uno nuevo vacío.
     */
    private void loadBanConfig() {
        // Si el archivo de configuración existe, lo carga
        if (configFile.exists()) {
            try (InputStream input = new FileInputStream(configFile)) {
                banConfig.load(input);
                // Carga los baneos en la estructura de datos
                for (String key : banConfig.stringPropertyNames()) {
                    String[] blockedUsersArray = banConfig.getProperty(key).split(",");
                    Set<String> blockedUsersSet = new HashSet<>(Arrays.asList(blockedUsersArray));
                    userBlocks.put(key, blockedUsersSet);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            // Si no existe, intenta crear el archivo de configuración
            saveBanConfig();
        }
    }

    /**
     * Guarda la configuración de baneos actuales en el archivo.
     */
    private void saveBanConfig() {
        // Guarda la configuración de baneos en el archivo
        try (OutputStream output = new FileOutputStream(configFile)) {
            banConfig.store(output, "Ban Configuration");
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    @Override
    public void broadcast(ChatMessage msg) {
        // Averiguamos id del remitente
        String senderUsername = idToUsername.get(msg.getId());

        // Creamos el mensaje con el formato "username: message"
        String formattedMessage = senderUsername + ": " + msg.getMessage();
        ChatMessage formattedChatMessage = new ChatMessage(msg.getId(), msg.getType(), formattedMessage);

        // Recorrer todos los clientes y enviamos el mensaje formateado,
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
        // Encuentra el ID correspondiente al username y lo elimina de idToUsername
        String usernameToRemove = idToUsername.get(id);
        if (usernameToRemove != null) {
            // Eliminar al cliente de los mapas y realizar cualquier otra limpieza necesaria.
            clients.remove(usernameToRemove);
            idToUsername.remove(id);
            System.out.println("El cliente " + usernameToRemove + " ha sido eliminado.");
        } else {
            System.out.println("El cliente con ID " + id + " no fue encontrado.");
        }
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

    private SimpleDateFormat sdf;
    private ChatServerImpl server; // Referencia al servidor para poder llamar a métodos como broadcast

    public ServerThreadForClient(Socket clientSocket, ChatServerImpl server, int clienteId, SimpleDateFormat sdf) {
        this.clientSocket = clientSocket;
        this.server = server;
        this.clientId = clienteId;
        this.sdf = sdf;

        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            System.out.println("Error al crear flujos de entrada/salida: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            sendAssignedClientId();      //Envía al cliente su id
            waitForUsername();           //Recibe nombre de usuario del cliente.
            processIncomingMessages();   // Procesa todos los mensajes siguientes del cliente en bucle inf.
        } catch (EOFException | SocketException e) {
            handleClientDisconnection(); // Cliente se desconecta inesperadamente.
        } catch (IOException | ClassNotFoundException e) {
            handleCommunicationError(e); // Error de I/O o de objetos
        } finally {
            cleanupResources();          // Limpia los recursos usados de una manera u otra.
        }
    }

    /**
     * Envía al cliente su ID asignado inmediatamente después de establecer la conexión.
     */
    private void sendAssignedClientId() throws IOException {
        out.writeObject(new ChatMessage(clientId, ChatMessage.MessageType.MESSAGE, "Tu id es: " + clientId));
    }

    /**
     * Espera el primer mensaje del cliente, que consideramos el nombre de usuario.
     */
    private void waitForUsername() throws IOException, ClassNotFoundException {
        ChatMessage initMessage = (ChatMessage) in.readObject();
        this.username = initMessage.getMessage();
        server.registerClient(this.clientId, this.username, this);
        System.out.println("Cliente " + this.username + " con id: " + this.clientId + " conectado.");
    }

    /**
     * Procesa los mensajes entrantes del cliente en un bucle continuo.
     */
    private void processIncomingMessages() throws IOException, ClassNotFoundException {
        while (true) {
            ChatMessage message = (ChatMessage) in.readObject();
            processMessage(message);
        }
    }

    /**
     * Procesa un mensaje individual dependiendo de su tipo.
     * @param message El mensaje recibido del cliente.
     */
    private void processMessage(ChatMessage message) throws IOException {
        if (message.getType() == ChatMessage.MessageType.MESSAGE) {
            handleChatMessage(message);
        } else if (message.getType() == ChatMessage.MessageType.LOGOUT) {
            throw new SocketException("Cliente solicitó la desconexión."); // Usamos SocketException para salir del bucle.
        }
    }

    /**
     * Maneja mensajes de chat, incluyendo comandos especiales como ban y unban.
     * @param message El mensaje de chat a procesar.
     */
    private void handleChatMessage(ChatMessage message) throws IOException {
        // Registro del mensaje en el servidor
        String timestamp = sdf.format(new Date());
        System.out.println(timestamp + " [" + this.username + "]: " + message.getMessage());

        String[] parts = message.getMessage().split("\\s+", 2);
        if ("ban".equalsIgnoreCase(parts[0]) && parts.length > 1) {
            server.addUserBlock(this.username, parts[1]);
            out.writeObject(new ChatMessage(clientId, ChatMessage.MessageType.MESSAGE, "usuario " + parts[1] + " bloqueado."));
        } else if ("unban".equalsIgnoreCase(parts[0]) && parts.length > 1) {
            server.removeUserBlock(this.username, parts[1]);
            out.writeObject(new ChatMessage(clientId, ChatMessage.MessageType.MESSAGE, "usuario " + parts[1] + " desbloqueado."));
        } else {
            server.broadcast(message);
        }
    }


    /**
     * Maneja la desconexión del cliente debido a cierre de socket o fin de la conexión.
     */
    private void handleClientDisconnection() {
        String timestamp = sdf.format(new Date());
        System.out.println(timestamp + " - " + this.username + " se ha desconectado con un cierre de socket.");
    }

    /**
     * Maneja errores de comunicación que no sean EOFException o SocketException.
     * @param e La excepción ocurrida.
     */
    private void handleCommunicationError(Exception e) {
        System.err.println("Error de comunicación con el cliente " + this.username + ": " + e.getMessage());
    }

    /**
     * Cierra los recursos y notifica al servidor la eliminación del cliente.
     */
    private void cleanupResources() {
        server.remove(this.clientId);
        closeConnection();
    }

    /**
     * Cierra la conexión y los flujos de entrada/salida asociados al cliente.
     */
    private void closeConnection() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (clientSocket != null) clientSocket.close();
        } catch (IOException e) {
            System.err.println("Error al cerrar los recursos del cliente " + this.username + ": " + e.getMessage());
        }
    }

    /**
     * Método para obtener el ObjectOutputStream asociado con este hilo de cliente
     */
    public ObjectOutputStream getOut() {
        return out;
    }

}



