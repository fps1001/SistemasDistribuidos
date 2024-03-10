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
 * @author Fernando Pisot Serrano
 * @email fps1001@alu.ubu.es
 */
public class ChatClientImpl implements ChatClient {
    private final String server;
    private final String username;
    private int port = 1500;

    ObjectOutputStream out = null; // cambio PrintWriter por la clase pedida.
    ObjectInputStream in = null; // cambio BufferReader por la clase pedida. Usado en hilo listener.
    // in leera del socket, stdin de teclado.
    BufferedReader stdIn = null;




    /**
     * Constructor con dirección IP/nombre de máquina y nickname.
     *
     * @param server dirección IP o nombre del servidor.
     * @param username apodo para el usuario.
     */
    public ChatClientImpl(String server, String username) {
        this.server = server;
        this.username = username;
        // Más inicializaciones según sea necesario...
    }

    @Override
    public void start() {

    }
    /**
     * Envía un mensaje al servidor.
     *
     * @param msg El mensaje a enviar.
     */
    @Override
    public void sendMessage(String msg) {
        try {
            // Envuelve el mensaje en un objeto ChatMessage y lo envía.
            out.writeObject(new ChatMessage(username, msg)); //TODO ESPERA 3 ARGUMENTOS ENCUENTRA 2.
            out.flush(); // Limpia el buffer para evitar problemas.
        } catch (IOException e) {
            System.err.println("Error al enviar mensaje: " + e.getMessage());
        }
    }

    @Override
    public void disconnect() {
        // Implementar desconexión...
    }

    /**
     * Punto de entrada principal para la aplicación del cliente.
     *
     * @param args argumentos de la línea de comandos.
     */
    public static void main(String[] args) {

        // Compruebo argumentos.
        if ((args.length != 1) || (args.length != 2)){
            System.err.println(
                    "Usage: java ChatClientImpl <host name> <port number>");
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
        new ChatClientImpl(server, nickname).start();
        //Arranco el hilo adicional a través de ChatClientLIstener.
        //new Thread(new ChatClientListener(in)).start();
        // Igual tiene que ir a connect, una vez establecido el socket entonces lanza el hilo que lee.




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
