package es.ubu.lsi.client;


import es.ubu.lsi.common.ChatMessage;
import es.ubu.lsi.common.ChatMessage.MessageType;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

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
    private boolean carryOn;
    private static final int DEFAULT_PORT = 1500;
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

        /**
     * Constructor con dirección IP/nombre de máquina y nickname.
     *
     * @param server dirección IP o nombre del servidor.
     * @param username apodo para el usuario.
     */
    public ChatClientImpl(String server, String username) {
        this.serverAddress = server;
        this.username = username;
        this.carryOn = true;
        try {
            this.clientSocket = new Socket(this.serverAddress, DEFAULT_PORT);
            out = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            System.err.println("ERROR: No se pudo iniciar el cliente.");
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
        //Lo primero será enviar el nombre de usuario.
        sendMessage(new ChatMessage(0, MessageType.MESSAGE, username));
        //Leeremos de servidor.
        // Inicializar la escucha de mensajes entrantes en un nuevo hilo
        try {
            in = new ObjectInputStream(clientSocket.getInputStream());
            new Thread(new ChatClientListener(in)).start(); // Este hilo se mantendrá activo escuchando mensajes del servidor.
        } catch (IOException e) {
            e.printStackTrace();
            disconnect(); // Desconectar si hay un error al iniciar.
        }

        // Función privada que funciona de distpacher de los mensajes recibidos.
        processUserInput();
    }

    /**
     * Recibirá un mensaje del servidor y tratará su información
     */
    private void processUserInput() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (carryOn) {
                System.out.print(username + ">>> "); // Añadir indicador de nombre de usuario antes de la entrada
                String input = scanner.nextLine();
                if ("salir".equalsIgnoreCase(input)) {
                    carryOn = false; // Cambiar la bandera carryOn para terminar los bucles en ambos hilos
                    sendMessage(new ChatMessage(0, MessageType.LOGOUT, "salir")); // Enviar mensaje de logout al servidor
                    disconnect(); // Desconectar del servidor
                } else {
                    sendMessage(new ChatMessage(0, MessageType.MESSAGE, input)); // Enviar mensajes regulares al servidor
                }
            }
            disconnect();
        } catch (Exception e) {
            System.err.println("Error al procesar la entrada del usuario: " + e.getMessage());
        }
    }

    /**
     * Envía un mensaje al servidor.
     *
     * @param msg El mensaje a enviar.
     */
    @Override
    public void sendMessage(ChatMessage msg) {
        try {
            out.writeObject(msg);
        } catch (IOException e) {
            System.err.println("ERROR: No se pudo enviar el mensaje al servidor.");
        }
    }

    /**
     * Desconecta al cliente del servidor, cerrando los flujos y el socket.
     */
    @Override
    public void disconnect() {
        try {
            this.carryOn = false;
            if (out != null) out.close();
            if (in != null) in.close();
            if (clientSocket != null) clientSocket.close();
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
        private final ObjectInputStream in; // Cambio BufferReader por la clase pedida. Usado en hilo listener.
        //private ObjectOutputStream out;


        public ChatClientListener(ObjectInputStream in){

            this.in = in;
        //this.new ObjectOutputStream();
        }

        @Override
        public void run() {
            // Escuchar y mostrar mensajes entrantes...
            while (carryOn){
                try {
                    ChatMessage msg = (ChatMessage) in.readObject();
                    String[] parts = msg.getMessage().split(": ", 2); // Suponemos que el mensaje tiene formato "username: message"
                    if (parts.length == 2) {
                        System.out.println(parts[0] + ">>> " + parts[1]); // Imprime "username>>> message"
                    } else {
                        System.out.println(msg.getMessage()); // Si no sigue el formato, imprime el mensaje como está
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Error al leer el mensaje: " + e.getMessage());
                    carryOn = false; // Cambiar la bandera para detener el bucle si hay un error
                }
            }
        }
    }
}
