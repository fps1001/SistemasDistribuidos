package es.ubu.lsi.server;

import java.net.ServerSocket;
import java.text.SimpleDateFormat;
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
    private AtomicInteger clientId;
    private SimpleDateFormat sdf;

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
        // Más inicializaciones según sea necesario...
    }

    @Override
    public void startup() {
        // Implementación del bucle del servidor con ServerSocket...
    }

    @Override
    public void broadcast(String message) {
        // Implementación de multidifusión de mensajes...
    }

    @Override
    public void remove(int id) {
        // Implementación de la eliminación de un cliente...
    }

    @Override
    public void shutdown() {
        // Implementación del apagado del servidor...
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
