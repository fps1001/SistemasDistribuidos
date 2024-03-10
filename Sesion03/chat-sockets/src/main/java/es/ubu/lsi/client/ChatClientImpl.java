package es.ubu.lsi.client;

import es.ubu.lsi.common.ChatMessage;

import java.io.*;
import java.net.Socket;

/**
 * Implementación del cliente para el sistema de chat.
 * Gestiona la conexión con el servidor y el envío y recepción de mensajes.
 * Utiliza la dirección IP o el nombre de la máquina y un nickname para la conexión.
 * El puerto por defecto es el 1500.
 *
 * Ejemplo de uso: java es.ubu.lsi.client.ChatClientImpl localhost aroca
 *
 * La clase ChatClientListener se define dentro de esta clase para facilitar el acceso a los campos y métodos de la clase externa.
 *
 * https://www.youtube.com/watch?v=gLfuZrrfKes
 *
 * @author Fernando Pisot Serrano
 * @email fps1001@alu.ubu.es
 */
public class ChatClientImpl implements ChatClient {
    private final String server;
    private final String username;
    private int port = 1500;
    //* Cambio a privadas y creo el socket.
    private ObjectOutputStream out = null; // cambio PrintWriter por la clase pedida.
    private ObjectInputStream in = null; // cambio BufferReader por la clase pedida. Usado en hilo listener.
    // in leera del socket, stdin de teclado.
    private BufferedReader stdIn = null;
    private Socket socket = null;

        /**
     * Constructor con dirección IP/nombre de máquina y nickname.
     *
     * @param server dirección IP o nombre del servidor.
     * @param username apodo para el usuario.
     */
    public ChatClientImpl(String server, String username) {
        this.server = server;
        this.username = username;
    }

    /**
     * Inicia la ejecución del cliente: establece la conexión con el servidor, arranca el hilo listener desde aquí.
     */
    @Override
    public void start() {
        try {
            //Inicializamos todas las variables que necesitamos: socket, objects in and out y lector de teclado:
            try (Socket socket = new Socket(server, port)) {
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
            }
            stdIn = new BufferedReader(new InputStreamReader(System.in));

            // TODO: Enviar mensaje del nombre de usuario aquí??? devuelve id???

            // Lanzamos el listener aquí!
            new Thread(new ChatClientListener(in)).start();

            String userInput;
            // TODO Leer mensaje en bucle
            // Lee mensajes de la consola y los envía al servidor y si es LOGOUT ir a disconnect.


        } catch (IOException e) {
            System.err.println("No se pudo iniciar el cliente: " + e.getMessage());
            disconnect();
        }
    }
    /**
     * Envía un mensaje al servidor.
     *
     * @param msg El mensaje a enviar.
     */
    @Override
    public void sendMessage(String msg) {
        try {
            // Determina el tipo de mensaje basado en el contenido del mensaje ignorando case.
            ChatMessage.MessageType type;
            if (msg.equalsIgnoreCase("logout")) {
                type = ChatMessage.MessageType.LOGOUT;
            } else if (msg.equalsIgnoreCase("shutdown")) {
                type = ChatMessage.MessageType.SHUTDOWN;
            } else {
                type = ChatMessage.MessageType.MESSAGE;
            }

            // Crea un objeto ChatMessage con el tipo y el mensaje.
            //TODO determinar el id del cliente aquí?
            ChatMessage chatMessage = new ChatMessage(id, type, msg);

            // Envía el objeto ChatMessage al servidor.
            out.writeObject(chatMessage);
            out.flush();
        } catch (IOException e) {
            System.err.println("Error al enviar mensaje: " + e.getMessage());
        }
    }

    /**
     * Desconecta al cliente del servidor, cerrando los flujos y el socket.
     */
    @Override
    public void disconnect() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (stdIn != null) stdIn.close();
            if (socket != null) socket.close();
            System.out.println("Desconectado del servidor.");
        } catch (IOException e) {
            System.err.println("Error al desconectar: " + e.getMessage());
        }
    }

    /**
     * Punto de entrada principal para la aplicación del cliente.
     *
     * @param args argumentos de la línea de comandos.
     */
    public static void main(String[] args) {

        // Compruebo argumentos.
        if ((args.length < 1) || (args.length > 2)){
            System.err.println(
                    "Usage: java ChatClientImpl (<host name>) <port number>");
            System.exit(1);
        }
        // Inicializo las variables y las copio de los argumentos pasados.
        String server = null;
        String nickname = null;

        if (args.length ==1){
            server = "localhost";
            nickname = args[0];
        }
        else {
            server = args[0];
            nickname = args[1];
        }

        //Arranco el hilo principal de ejecución para el cliente anónimamente.
        ChatClientImpl client = new ChatClientImpl(server, nickname);
        client.start();

    }

    /**
     * Clase interna que escucha los mensajes del servidor y los muestra al cliente.
     * Se ejecuta en su propio hilo para no bloquear la interfaz de usuario del cliente.
     *
     * Como es una clase interna, tiene acceso directo a los métodos y campos de la clase externa ChatClientImpl.
     */
    private class ChatClientListener implements Runnable {

        ObjectInputStream in;
        public ChatClientListener (ObjectInputStream in){
            this.in = in; // para recoger información del servidor.
        }

        @Override
        public void run() {
            // Escuchar y mostrar mensajes entrantes...
            while (true){ //TODO: quizá usar flag para evitar warning?
                try { // Obtengo mensaje y lo imprimo.
                    String msg = ((ChatMessage) in.readObject()).getMessage();
                    System.out.println(msg);
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Error al leer el mensaje: " + e.getMessage());
                    break; // Sale del bucle si hay un error.
                }
            }
        }
    }
}
