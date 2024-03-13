package es.ubu.lsi.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * Implementación principal del servidor de chat.
 * Gestiona las conexiones entrantes y la creación de hilos para cada cliente.
 * Por defecto, el servidor escucha en el puerto 1500.
 *
 * Ejemplo de uso: java es.ubu.lsi.server.ChatServerImpl
 *
 * @author Fernando Pisot Serrano
 * @email fps1001@alu.ubu.es
 * @repo https://github.com/fps1001/SistemasDistribuidos
 *
 * https://socket.io/docs/v3/broadcasting-events
 */
//public class ChatServerImpl implements ChatServer {
//
//    private static final int DEFAULT_PORT = 1500;
//    private ServerSocket serverSocket;
//    private boolean alive;
//    private AtomicInteger clientId; //atomic para que no se repitan.
//    private SimpleDateFormat sdf;
//    //Voy a usar un ConcurrentHashMap que puedes emparejar un entero con una clase personalizada como el hilo del cliente.
//    //Así será más fácil enviar a todos o desconectar al que sea necesario.
//    private ConcurrentHashMap<Integer, ServerThreadForClient> clientMap;
//
//    /**
//     * Constructor de clase que inicializa las variables.
//     */
//    public ChatServerImpl() {
//        this.alive = true;
//        this.clientId = new AtomicInteger(0);
//        this.sdf = new SimpleDateFormat("HH:mm:ss");
//        this.clientMap = new ConcurrentHashMap<>(); // Lo inicializo para evitar nullpointerexcept.
//        try {
//            this.serverSocket = new ServerSocket(DEFAULT_PORT);
//            System.out.println("El servidor está escuchando en el puerto " + DEFAULT_PORT);
//        } catch (IOException e) {
//            System.err.println("Error al iniciar el servidor: " + e.getMessage());
//            shutdown();
//        }
//    }
//
//
//    /**
//     * Punto de entrada principal para la aplicación del servidor.
//     *
//     * @param args argumentos de la línea de comandos.
//     */
//    public static void main(String[] args) throws IOException {
//
//        // Creamos un servidor de la clase de este archivo java y ejecutamos startup.
//        ChatServerImpl server = new ChatServerImpl();
//        server.startup();
//    }
//
//    /**
//     * Implementa el bucle con el servidor de sockets.
//     * Ante una petición, se instancia un nuevo hilo y se arranca el hilo correspondiente
//     * para cada cliente con los objetos buffers.
//     */
//    @Override
//    public void startup() {
//        // Copio el sistema del servidor multihilo
//        try {
//            serverSocket = new ServerSocket(DEFAULT_PORT);
//            System.out.println("El servidor está escuchando en el puerto " + DEFAULT_PORT);
//
//            while (alive) {
//                // Acepta una nueva conexión de cliente
//                Socket clientSocket = serverSocket.accept();
//                System.out.println("Un nuevo cliente se ha conectado.");
//                // Incrementa y obtiene el nuevo ID de cliente
//                int newClientId = clientId.incrementAndGet();
//                // Crea un nuevo thread para manejar la comunicación con el cliente
//                ServerThreadForClient clientThread = new ServerThreadForClient(newClientId, clientSocket, this);
//                // Añado al mapa el par: id del cliente y su hilo.
//                clientMap.put(newClientId, clientThread);
//
//                // Inicio el hilo para este cliente
//                clientThread.start();
//
//                System.out.println("Nuevo cliente conectado: " + " (ID: " + newClientId + ")");
//                //Vuelta a esperar hasta salida de bucle infinito.
//            }
//        } catch (IOException e) {
//            System.err.println("Error al iniciar el servidor: " + e.getMessage());
//            this.shutdown();
//        }
//    }
//
//    @Override
//    public void broadcast(String msg) {
//        // Recorrer el mapa para enviar el mensaje a todos los clientes...
//        //TODO Quizá habría que evitar el remitente??? id=0, creo que no está muy bien.
//        clientMap.values().forEach(client -> client.sendMessage(new ChatMessage(0, ChatMessage.MessageType.MESSAGE, msg)));
//    }
//
//    @Override
//    public void remove(int id) {
//        // Elimina el cliente del mapa y cierra su conexión
//        ServerThreadForClient clientThread = clientMap.remove(id);
//        if (clientThread != null) {
//            clientThread.shutdown();
//            System.out.println("Cliente eliminado: ID " + clientId);
//        } else {
//            System.out.println("Intento de eliminar un cliente no existente: ID " + clientId);
//        }
//    }
//
//    /**
//     * Cierra todas las conexiones de cliente y luego apaga el servidor.
//     * Cierra los sockets de cliente, sus flujos de entrada y salida asociados,
//     * y finalmente el socket del servidor.
//     */
//    @Override
//    public void shutdown() {
//        System.out.println("Apagando el servidor...");
//        // Marca el servidor como no vivo para detener el bucle principal del servidor.
//        alive = false;
//
//        // Cierra todas las conexiones de cliente.
//        clientMap.forEach((clientId, clientThread) -> {
//            System.out.println("Cerrando la conexión con el cliente ID: " + clientId);
//            clientThread.shutdown(); // Invoca el método shutdown de cada cliente, que debería cerrar los recursos de red.
//        });
//
//        clientMap.clear(); // Limpia el mapa después de cerrar todas las conexiones de cliente.
//
//        // Cierra el ServerSocket del servidor.
//        try {
//            if (serverSocket != null && !serverSocket.isClosed()) {
//                serverSocket.close();
//            }
//            System.out.println("El servidor ha sido apagado correctamente.");
//        } catch (IOException e) {
//            System.err.println("Error al cerrar el ServerSocket del servidor: " + e.getMessage());
//        }
//    }
//
//
//
//    /**
//     * Clase interna que maneja la comunicación con un cliente específico en el sistema de chat.
//     * Se ejecuta como un hilo separado para manejar las solicitudes del cliente individualmente.
//     */
//    private class ServerThreadForClient extends Thread {
//        private final int clientId;
//        private String username;
//        private boolean alive;
//        private Socket socket;
//        private ObjectInputStream in;
//        private ObjectOutputStream out;
//
//        private ChatServerImpl server;
//
//        public ServerThreadForClient(int clientId, Socket socket, ChatServerImpl server) {
//            this.clientId = clientId;
//            this.socket = socket;
//            this.server = server;
//            try {
//                out = new ObjectOutputStream(socket.getOutputStream());
//                in = new ObjectInputStream(socket.getInputStream());
//            } catch (IOException e) {
//                System.err.println("Error al establecer flujos de E/S para el cliente " + clientId);
//            }
//        }
//
//        @Override
//        public void run() {
//            try {
//                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
//
//                while (alive) {
//                    // Leer mensaje del cliente
//                    ChatMessage msg = (ChatMessage) in.readObject();
//                    // Procesar mensaje según su tipo
//                    switch (msg.getType()) {
//                        case MESSAGE:
//                            // Reenvia mensaje a todos los clientes
//                            server.broadcast(msg.getMessage());
//                            break;
//                        case LOGOUT:
//                            // Maneja desconexión del cliente
//                            server.remove(this.clientId);
//                            return; // Terminar ejecución del hilo
//                        case SHUTDOWN:
//                            // Inicia secuencia de apagado del servidor
//                            server.shutdown();
//                            break;
//                        //TODO DROP / BAN.... HAY QUE AÑADIR MÁS LÓGICA UNA VEZ FUNCIONE ESTO
//                    }
//                }
//            } catch (IOException | ClassNotFoundException e) {
//                e.printStackTrace();
//            } finally {
//                // Limpia recursos y eliminar cliente del mapa
//                server.remove(this.clientId);
//            }
//        }
//
//        public void sendMessage(ChatMessage message) {
//            try {
//                out.writeObject(message);
//                out.flush();
//            } catch (IOException e) {
//                System.err.println("Error al enviar mensaje al cliente " + clientId + ": " + e.getMessage());
//            }
//        }
//
//        public void shutdown() {
//            // Intenta cerrar el socket del cliente y sus flujos asociados.
//            try {
//                if (this.socket != null && !this.socket.isClosed()) {
//                    serverSocket.close(); // Cierra el socket del cliente.
//                }
//                System.out.println("Conexión cerrada para el cliente ID: " + this.clientId);
//            } catch (IOException e) {
//                System.err.println("Error al cerrar la conexión para el cliente ID: " + this.clientId + ": " + e.getMessage());
//            } finally {
//                alive = false; // Asegura que el hilo del bucle se detenga.
//            }
//        }
//
//    }
//}


//package es.ubu.lsi.server;

/*
 * Copyright (c) 2013, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.io.*;
import java.util.Arrays;

public class ChatServerImpl implements ChatServer {

    //FPS Lista negra de puertos.
    private static final int[] BLACKLISTED_PORTS = {1234, 5678};
    private static final int DEFAULT_PORT = 1500;

    public static void main(String[] args) throws IOException {

        if (args.length != 0) {
            System.err.println("Usage: java EchoServer <port number>");
            System.exit(1);
        }

        int portNumber = DEFAULT_PORT;
        System.out.println("Escuchando por puerto: " + portNumber);

        try  (
                //FPS Cambio por claridad la variable, en vez de volver a hacer el parse
                //ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));
                ServerSocket serverSocket = new ServerSocket(portNumber);
        )
        {
            while (true){
                Socket clientSocket = serverSocket.accept();
                //FPS Comprobación de lista negra.
                if (isPortBlacklisted(clientSocket.getPort())) { // Utilizo un método que devuelve true si está en blacklist
                    System.out.println("Conexión rechazada desde el puerto bloqueado: " + clientSocket.getPort());
                    clientSocket.close(); // Cierra la conexión si está en la lista negra.
                    continue; // Vuelve a la siguiente iteración de bucle.
                }

                // Si pasa el corte establece la conexión.
                System.out.println("Nuevo Cliente: " + clientSocket.getInetAddress() + "/" + clientSocket.getPort());
                Thread hilonuevocliente = new ServerThreadForClient(clientSocket);
                hilonuevocliente.start();
            }

        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }

    //FPS Método auxiliar para verificar si un puerto está en la lista negra
    private static boolean isPortBlacklisted(int port) {
        return Arrays.stream(BLACKLISTED_PORTS).anyMatch(blacklistedPort -> blacklistedPort == port);
    }

    @Override
    public void startup() {

    }

    @Override
    public void broadcast(String msg) {

    }

    @Override
    public void remove(int id) {

    }

    @Override
    public void shutdown() {

    }
}

class ServerThreadForClient extends Thread {

    private final Socket clientSocket;

    public ServerThreadForClient(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                System.out.println(clientSocket.getPort() + ":" + inputLine);
                out.println(inputLine);
            }
        }
        catch (IOException e) {
            System.out.println("Exception caught on thread");
            System.out.println(e.getMessage());
        }
    }
}