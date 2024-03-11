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
 * https://www.youtube.com/watch?v=gLfuZrrfKes - Video y canal de WittCode
 *
 * @author Fernando Pisot Serrano
 * @email fps1001@alu.ubu.es
 * @repo https://github.com/fps1001/SistemasDistribuidos
 */
public class ChatClientImpl implements ChatClient {
    private String serverAddress;
    private String username;
    private boolean alive; //* Igual tiene que ser en el hilo.
    private static final int DEFAULT_PORT = 1500;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

        /**
     * Constructor con dirección IP/nombre de máquina y nickname.
     *
     * @param server dirección IP o nombre del servidor.
     * @param username apodo para el usuario.
     */
    public ChatClientImpl(String server, String username) {
        try {
            this.serverAddress = server;
            this.username = username;

        } catch (Exception e) {
            System.out.println("Error al conectar con el servidor: " + e.getMessage());
            System.exit(1);
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

        //Arranco el hilo principal de ejecución para el cliente.
        ChatClientImpl client = new ChatClientImpl(server, nickname);
        client.start();

    }

    /**
     * Inicia la ejecución del cliente: establece la conexión con el servidor, arranca el hilo listener desde aquí.
     */
    @Override
    public void start() { // Hilo principal del cliente:
        try {
            // Establecemos conexión con el servidor
            socket = new Socket(serverAddress, DEFAULT_PORT);
            System.out.println("Conectado al servidor de chat en " + serverAddress + ":" + DEFAULT_PORT);

            // Inicializo aquí los flujos de entrada y salida
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            // Enviar el nombre de usuario al servidor como el primer mensaje
            sendMessage(username);
            //sendMessage(new ChatMessage(0, ChatMessage.MessageType.LOGIN, username));

            // Crea y arranca el hilo para escuchar mensajes del servidor
            new Thread(new ChatClientListener(in)).start();
        } catch (Exception e) {
            System.err.println("Error al iniciar el cliente: " + e.getMessage());
            System.exit(1);
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
            //TODO como determino el id del cliente aquí?
            ChatMessage chatMessage = new ChatMessage(0, type, msg);

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
            if (socket != null) socket.close();
            System.out.println("Desconectado del servidor.");
        } catch (IOException e) {
            System.err.println("Error al desconectar: " + e.getMessage());
        }
    }



    /**
     * Clase interna que escucha los mensajes del servidor y los muestra al cliente.
     * Se ejecuta en su propio hilo para no bloquear la interfaz de usuario del cliente.
     *
     * Como es una clase interna, tiene acceso directo a los métodos y campos de la clase externa ChatClientImpl.
     */
    private class ChatClientListener implements Runnable {
        private ObjectInputStream in = null; // cambio BufferReader por la clase pedida. Usado en hilo listener.
        // in leera del socket, stdin de teclado.

        public ChatClientListener(ObjectInputStream input){
            this.in = input;
        }

        @Override
        public void run() {
            // Escuchar y mostrar mensajes entrantes...
            while (alive){ //TODO: quizá usar flag para evitar warning?
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
