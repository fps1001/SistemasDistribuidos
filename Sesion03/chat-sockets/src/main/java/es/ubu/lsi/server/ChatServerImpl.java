package es.ubu.lsi.server;


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
        this.clientMap = new ConcurrentHashMap<>(); // Lo inicializo para evitar nullpointerexcept.
    }


    /**
     * Punto de entrada principal para la aplicación del servidor.
     *
     * @param args argumentos de la línea de comandos.
     */
    public static void main(String[] args) throws IOException {

        // Creamos un servidor de la clase de este archivo java y ejecutamos startup.
        ChatServerImpl server = new ChatServerImpl(DEFAULT_PORT);
        server.startup();
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
                //System.out.println("Un nuevo cliente se ha conectado.");
                // Incrementa y obtiene el nuevo ID de cliente
                int newClientId = clientId.incrementAndGet();

                //TODO Aquí se debería realizar alguna lógica para obtener el username del cliente,
                // por ejemplo, leer el primer mensaje enviado tras la conexión.
                // Para este ejemplo, usaremos el ID del cliente como placeholder.
                String username = "Cliente" + newClientId;

                // Crea un nuevo thread para manejar la comunicación con el cliente
                ServerThreadForClient clientThread = new ServerThreadForClient(username, newClientId, this.serverSocket);
                // Añado al mapa el par: id del cliente y su hilo.
                clientMap.put(newClientId, clientThread);

                // Inicio el hilo para este cliente
                clientThread.start();

                System.out.println("Nuevo cliente conectado: " + username + " (ID: " + newClientId + ")");
                //Vuelta a esperar hasta salida de bucle infinito.
            }
        } catch (IOException e) {
            System.err.println("Error al iniciar el servidor: " + e.getMessage());
            //this.shutdown();
        }
    }

    @Override
    public void broadcast(String msg) {
        // Recorrer el mapa para enviar el mensaje a todos los clientes...
        //TODO Quizá habría que evitar el remitente???
        clientMap.values().forEach(client -> client.sendMessage(msg));
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

    /**
     * Cierra todas las conexiones de cliente y luego apaga el servidor.
     * Cierra los sockets de cliente, sus flujos de entrada y salida asociados,
     * y finalmente el socket del servidor.
     */
    @Override
    public void shutdown() {
        System.out.println("Apagando el servidor...");
        // Marca el servidor como no vivo para detener el bucle principal del servidor.
        alive = false;

        // Cierra todas las conexiones de cliente.
        clientMap.forEach((clientId, clientThread) -> {
            System.out.println("Cerrando la conexión con el cliente ID: " + clientId);
            clientThread.shutdown(); // Invoca el método shutdown de cada cliente, que debería cerrar los recursos de red.
        });

        clientMap.clear(); // Limpia el mapa después de cerrar todas las conexiones de cliente.

        // Cierra el ServerSocket del servidor.
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            System.out.println("El servidor ha sido apagado correctamente.");
        } catch (IOException e) {
            System.err.println("Error al cerrar el ServerSocket del servidor: " + e.getMessage());
        }
    }



    /**
     * Clase interna que maneja la comunicación con un cliente específico en el sistema de chat.
     * Se ejecuta como un hilo separado para manejar las solicitudes del cliente individualmente.
     */
    private class ServerThreadForClient extends Thread {
        private int id;
        private String username;
        private boolean alive;
        private ServerSocket serverSocket;

        public ServerThreadForClient(String username, int id, ServerSocket serverSocket) {
            this.id = id;
            this.username = username;
            this.serverSocket = serverSocket;
            this.alive = true;
        }

        @Override
        public void run() {
//  no sé qué hay que hacer aquí!


        }
        public void shutdown() {
            // Intenta cerrar el socket del cliente y sus flujos asociados.
            try {
                if (this.serverSocket != null && !this.serverSocket.isClosed()) {
                    serverSocket.close(); // Cierra el socket del cliente.
                }
                System.out.println("Conexión cerrada para el cliente ID: " + this.id);
            } catch (IOException e) {
                System.err.println("Error al cerrar la conexión para el cliente ID: " + this.id + ": " + e.getMessage());
            } finally {
                alive = false; // Asegura que el hilo del bucle se detenga.
            }
        }

    }
}
