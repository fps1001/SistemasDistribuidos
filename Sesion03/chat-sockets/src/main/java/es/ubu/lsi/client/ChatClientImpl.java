package es.ubu.lsi.client;

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
    private String serverAddress;
    private String nickname;
    private final int port = 1500;

    // ...otros campos necesarios para la implementación...

    /**
     * Constructor con dirección IP/nombre de máquina y nickname.
     *
     * @param serverAddress dirección IP o nombre del servidor.
     * @param nickname apodo para el usuario.
     */
    public ChatClientImpl(String serverAddress, String nickname) {
        this.serverAddress = serverAddress;
        this.nickname = nickname;
        // Más inicializaciones según sea necesario...
    }

    @Override
    public void start() {
        // Iniciar ChatClientListener...
        // Más implementación...
    }

    @Override
    public void sendMessage(String message) {
        // Implementar envío de mensajes...
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
        // Iniciar cliente...
    }

    /**
     * Clase interna que escucha los mensajes del servidor y los muestra al cliente.
     * Se ejecuta en su propio hilo para no bloquear la interfaz de usuario del cliente.
     *
     * Como es una clase interna, tiene acceso directo a los métodos y campos de la clase externa ChatClientImpl.
     */
    private class ChatClientListener implements Runnable {

        @Override
        public void run() {
            // Escuchar y mostrar mensajes entrantes...
        }
    }
}
