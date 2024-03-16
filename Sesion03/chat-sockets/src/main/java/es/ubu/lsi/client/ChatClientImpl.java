package es.ubu.lsi.client;


import es.ubu.lsi.common.ChatMessage;
import es.ubu.lsi.common.ChatMessage.MessageType;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

/**
 * Implementación del cliente para el sistema de chat.
 * Gestiona la conexión con el servidor y el envío y recepción de mensajes.
 * Utiliza la dirección IP o el nombre de la máquina y un nickname para la conexión.
 * El puerto por defecto es el 1500.
 * <p>
 * Ejemplo de uso: java es.ubu.lsi.client.ChatClientImpl localhost aroca
 * <p>
 * La clase ChatClientListener se define dentro de esta clase para facilitar el acceso a los campos y métodos de la clase externa.
 * <p>
 * <a href="https://www.youtube.com/watch?v=gLfuZrrfKes">...</a> - Video y canal de WittCode
 *
 * @author Fernando Pisot Serrano
 * @email fps1001@alu.ubu.es
 * @repo <a href="https://github.com/fps1001/SistemasDistribuidos">...</a>
 */
public class ChatClientImpl implements ChatClient {
    private String serverAddress;
    private String username;
    private boolean carryOn;
    private static final int DEFAULT_PORT = 1500;
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    int id;

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
        try {
            // Espera recibir el id asignado por el servidor.
            in = new ObjectInputStream(clientSocket.getInputStream());
            ChatMessage welcomeMessage = (ChatMessage) in.readObject();
            // Suponiendo que el servidor envía un mensaje indicando el id del cliente.
            this.id = welcomeMessage.getId(); // Actualiza el id del cliente basado en el mensaje recibido.
            System.out.println(welcomeMessage.getMessage()); // Muestra el mensaje con el id asignado.

            //Lo primero será enviar el nombre de usuario.
            sendMessage(new ChatMessage(this.id, MessageType.MESSAGE, username));
            //Leeremos de servidor su id.

            // Inicializar la escucha de mensajes entrantes en un nuevo hilo

            //in = new ObjectInputStream(clientSocket.getInputStream());
            new Thread(new ChatClientListener(in)).start(); // Este hilo se mantendrá activo escuchando mensajes del servidor.
        } catch (IOException | ClassNotFoundException e) {
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
            System.out.println("Bienvenido al chat: " + username ); // Añadir indicador de nombre de usuario antes de la entrada
            while (carryOn) {
                String input = scanner.nextLine();
                if ("logout".equalsIgnoreCase(input)) {
                    carryOn = false; // Cambiar la bandera carryOn para terminar los bucles en ambos hilos
                    sendMessage(new ChatMessage(this.id, MessageType.LOGOUT, "salir")); // Enviar mensaje de logout al servidor
                    disconnect(); // Desconectar del servidor
                    System.out.println("Desconectando del servidor.");
                } else {
                    sendMessage(new ChatMessage(this.id, MessageType.MESSAGE, input)); // Enviar mensajes regulares al servidor
                }
            }
        } catch (Exception e) {
            System.err.println("Error al procesar la entrada del usuario: " + e.getMessage());
        } finally {
            disconnect(); // Llamar a disconnect fuera del bucle para cerrar recursos
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
        // Enviar un mensaje al servidor que el cliente está desconectando
//        if (out != null) {
//            try {
//                out.writeObject(new ChatMessage(this.id, MessageType.LOGOUT, "Cliente desconectando"));
//            } catch (IOException e) {
//                // Logear el error, el servidor podría haberse desconectado primero
//            }
//        }

        // Cerrar los recursos
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (clientSocket != null) clientSocket.close();
        } catch (IOException e) {
            System.err.println("Error al cerrar los recursos del cliente: " + e.getMessage());
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
//                    ChatMessage msg = (ChatMessage) in.readObject();
//                    String[] parts = msg.getMessage().split(": ", 2); // Suponemos que el mensaje tiene formato "username: message"
//                    System.out.println(parts[0] + ">>> " + parts[1]); // Imprime "username>>> message"
                    ChatMessage msg = (ChatMessage) in.readObject();
                    // Puede que no necesites dividir el mensaje en partes si simplemente vas a imprimirlo
                    System.out.println(msg.getMessage()); // Imprime el mensaje completo tal cual se recibe
                } catch (EOFException e) {
                    // Esto se espera cuando el socket se cierra mientras se está esperando leer. Salimos del bucle.
                    break;
                } catch (SocketException e) {
                    if (!carryOn) { // Si está a falso es que se cerró intencionadamente, sin problema.
                        break;
                    } else {
                        // Si el flag está a true es un fallo unexpected socket error.
                        System.out.println("Error al leer el mensaje: " + e.getMessage());
                        break;
                    }
                } catch (IOException | ClassNotFoundException e) {
                    // Para otras excepciones escribimos error
                    System.out.println("Error al leer el mensaje: " + e.getMessage());
                    break;
                }
            }
        }
    }
}
