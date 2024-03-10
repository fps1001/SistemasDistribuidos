package es.ubu.lsi.server;

import es.ubu.lsi.common.ChatMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Implementación principal del servidor de chat.
 * Gestiona las conexiones entrantes y la creación de hilos para cada cliente.
 * Por defecto, el servidor escucha en el puerto 1500.
 *
 * Ejemplo de uso: java es.ubu.lsi.server.ChatServerImpl
 *
 * @author Fernando Pisot Serrano
 * @email fps1001@alu.ubu.es
 */
public class ChatServerImpl implements ChatServer {

    private static final int DEFAULT_PORT = 1500;
    private int port;
    private ServerSocket serverSocket;
    private boolean alive;
    //atomic para que no se repitan.
    private AtomicInteger clientId;
    private SimpleDateFormat sdf;
    //Voy a usar un ConcurrentHashMap que puedes emparejar un entero con una clase personalizada como el hilo del cliente.
    //Así será más fácil enviar a todos o desconectar al que sea necesario.
    private ConcurrentHashMap<Integer, ServerThreadForClient> clientMap;

    /**
     * Constructor con el puerto de escucha para el servidor.
     *
     * @param port Puerto de escucha.
     */
    public ChatServerImpl(int port) {
        this.port = port;
        this.alive = true;
        this.clientId = new AtomicInteger(0);
        this.sdf = new SimpleDateFormat("HH:mm:ss");
        // Voy a usar un concurrentHashMap para
    }

    /**
     * Implementa el bucle con el servidor de sockets.
     * Ante una petición, se instancia un nuevo hilo y se arranca el hilo correspondiente
     * para cada cliente con los objetos buffers.
     */
    @Override
    public void startup() {
        // Copio el sistema del servidor multihilo
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("El servidor está escuchando en el puerto " + port);

            while (alive) {
                // Acepta una nueva conexión de cliente
                Socket clientSocket = serverSocket.accept();
                // Incrementa y obtiene el nuevo ID de cliente
                int newClientId = clientId.incrementAndGet();

                //TODO Aquí se debería realizar alguna lógica para obtener el username del cliente,
                // por ejemplo, leer el primer mensaje enviado tras la conexión.
                // Para este ejemplo, usaremos el ID del cliente como placeholder.
                String username = "Cliente" + newClientId;

                // Crea un nuevo thread para manejar la comunicación con el cliente
                ServerThreadForClient clientThread = new ServerThreadForClient(clientSocket, username, newClientId);
                // Añado al mapa el id del cliente asociado a su hilo.
                clientMap.put(newClientId, clientThread);

                // Inicio el hilo para este cliente
                clientThread.start();

                System.out.println("Nuevo cliente conectado: " + username + " (ID: " + newClientId + ")");
            }
        } catch (IOException e) {
            System.err.println("Error al iniciar el servidor: " + e.getMessage());
            shutdown();
        }
    }

    @Override
    public void broadcast(String msg) {
        // Recorrer el mapa para enviar el mensaje a todos los clientes...
        //TODO Quizá habría que evitar el remitente???
        clientMap.values().forEach(client -> client.sendMessage(message));
    }

    @Override
    public void remove(int id) {
        // Elimina el cliente del mapa y cierra su conexión
        ServerThreadForClient clientThread = clientMap.remove(id);
        if (clientThread != null) {
            clientThread.shutdown();
            System.out.println("Cliente eliminado: ID " + clientId);
        } else {
            System.out.println("Intento de eliminar un cliente no existente: ID " + clientId);
        }
    }

    @Override
    public void shutdown() {
        // Cierra la conexión con el cliente y baja el flag de bucle de escucha.
        alive = false;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            System.err.println("Error al cerrar el socket del cliente: " + e.getMessage());
        }
    }

    /**
     * Punto de entrada principal para la aplicación del servidor.
     *
     * @param args argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;
        ChatServerImpl server = new ChatServerImpl(port);
        server.startup();
    }

    /**
     * Clase interna que maneja la comunicación con un cliente específico en el sistema de chat.
     * Se ejecuta como un hilo separado para manejar las solicitudes del cliente individualmente.
     */
    private class ServerThreadForClient extends Thread {
        private int id;
        private String username;
        private boolean alive;

        public ServerThreadForClient(String username, int id) {
            this.id = id;
            this.username = username;
            this.alive = true;
        }

        @Override
        public void run() {
            while(alive) {
                // Manejar la comunicación con el cliente
            }
        }

        // Métodos adicionales si es necesario...
    }
}
