package es.ubu.lsi.client;

import java.io.*;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import es.ubu.lsi.common.ChatMessage;
import es.ubu.lsi.server.ChatServer;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * ChatClientImpl: implementación concreta del cliente en el chat.
 * Esta clase se encarga de la lógica del lado del cliente, permitiendo al usuario
 * conectarse al servidor, enviar y recibir mensajes.
 * <p>
 * Para más información sobre el proyecto, consultar el repositorio de GitHub.
 * Contacto: fps1001@alu.ubu.es
 *
 * @see <a href="https://github.com/fps1001/SistemasDistribuidos">Repositorio de GitHub</a>
 * @author Fernando Pisot Serrano
 */

//public class ChatClientImpl extends UnicastRemoteObject implements ChatClient {
public class ChatClientImpl implements ChatClient, Serializable {

    /** Id del servidor al cliente.*/
    private int id = 1; // Añade un serialVersionUID
    /** Nickname del usuario.*/
    private String nickName;
    /** Referencia al servidor de chat */
    private ChatServer server;
    /** Fecha de envío de mensaje */
    private static SimpleDateFormat sdf = new SimpleDateFormat ("HH:mm:ss");
    /** Cuando sufra un drop avisará de que no está conectado*/
    private boolean isConnected;

    /**
     * Constructor de ChatClientImpl.
     *
     * @param nickName El nickname del usuario.
     * @param server La referencia al servidor de chat.
     * @throws RemoteException Si ocurre un error en la llamada remota.
     */
    public ChatClientImpl(String nickName, ChatServer server) throws RemoteException {
        super();
        this.nickName = nickName;
        this.server = server;
        this.isConnected = true;
    }

    /**
     * Obtiene el ID del cliente.
     *
     * @return El ID del cliente.
     * @throws RemoteException Si ocurre un error en la llamada remota.
     */
    @Override
    public int getId() throws RemoteException {
        return id;
    }

    /**
     * Establece el ID del cliente.
     *
     * @param id El nuevo ID del cliente.
     * @throws RemoteException Si ocurre un error en la llamada remota.
     */
    @Override
    public void setId(int id) throws RemoteException {
        this.id = id;
    }

    /**
     * Obtiene el nickname del cliente.
     *
     * @return El nickname del cliente.
     * @throws RemoteException Si ocurre un error en la llamada remota.
     */
    @Override
    public String getNickName() throws RemoteException {
        return nickName;
    }

    /**
     * Recibe un mensaje del servidor y lo muestra.
     *
     * @param msg El mensaje recibido del servidor.
     * @throws RemoteException Si ocurre un error en la llamada remota.
     */
    @Override
    public void receive(ChatMessage msg) throws RemoteException {
        if("logout".equals(msg.getMessage())) {
            System.out.println("Has sido eliminado del servidor.");
            this.disconnect(); // Realiza la desconexión y limpieza
            System.exit(0); // Cierra la ejecución del cliente inmediatamente
        } else {
            // Mostrar el mensaje recibido en la consola del cliente o en la interfaz de usuario.
            String sender = msg.getId() == 0 ? "Server" : msg.getNickname(); // Si es id=0 es el servidor informando de un drop.
            System.out.println(getSdf() + "- " + sender + ": " + msg.getMessage());
        }
    }


    /**
     * Envía un mensaje al chat a través del servidor.
     *
     * @param message El mensaje a enviar.
     * @throws RemoteException Si ocurre un error en la llamada remota.
     */
    public void sendMessage(String message) throws RemoteException {
        if (!isConnected) {
            System.out.println("No puedes enviar mensajes porque estás desconectado.");
            System.exit(0);
        } else {
            // Enviar el mensaje solo si el cliente está conectado
            ChatMessage msg = new ChatMessage(this.id, this.nickName, message);
            server.publish(msg);
        }
    }

    /**
     * Desconecta al cliente del servidor de chat y realiza la limpieza necesaria.
     *
     * @throws RemoteException Si ocurre un error en la llamada remota.
     */
    public void disconnect() {
        try {
            server.logout(this);
            UnicastRemoteObject.unexportObject(this, true);
        } catch (NoSuchObjectException e) {
            System.out.println("Ya desconectado.");
        } catch (RemoteException e) {
    }
        isConnected = false;
    }


    /** getServer. Getter de servidor.*/
    public ChatServer getServer() {
        return server;
    }
    /** setServer. Setter de servidor.*/
    public void setServer(ChatServer server) {
        this.server = server;
    }
    /** getSdf. Getter de hora.*/
    public static String getSdf() {
        return sdf.format(new Date());
    }
}
