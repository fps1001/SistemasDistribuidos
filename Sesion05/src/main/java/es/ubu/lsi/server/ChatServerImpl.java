package es.ubu.lsi.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;

import es.ubu.lsi.client.ChatClient;
import es.ubu.lsi.common.ChatMessage;

/**
 * ChatServerImpl: Implementación del servidor RMI.
 * Maneja la lógica del registro de clientes, publicar mensajes y logout.
 *
 * <p>
 * Para más información sobre el proyecto, consultar el repositorio de GitHub.
 * Contacto: fps1001@alu.ubu.es
 *
 * @see <a href="https://github.com/fps1001/SistemasDistribuidos">Repositorio de GitHub</a>
 * @author Fernando Pisot Serrano
 */

public class ChatServerImpl extends UnicastRemoteObject implements ChatServer {

    // Lista de clientes conectados: como en sockets usaré un mapa id-cliente.
    private ConcurrentHashMap<Integer, ChatClient> clients = new ConcurrentHashMap<>();
    private int clientIdCounter = 1; // Contador para asignar ID únicos a cada cliente.

    public ChatServerImpl() throws RemoteException {
        super();
    }

    /**
     * Registra un nuevo cliente en el servidor.
     *
     * @param client Cliente a registrar.
     * @return ID asignado al cliente.
     * @throws RemoteException Si ocurre un error en la llamada remota.
     */
    @Override
    public synchronized int checkIn(ChatClient client) throws RemoteException {
        int clientId = clientIdCounter++;
        clients.put(clientId, client); // añado a mapa el cliente con su id.
        System.out.println("Cliente registrado con ID: " + clientId);
        return clientId;
    }

    /**
     * Desconecta un cliente del servidor.
     *
     * @param client Cliente a desconectar.
     * @throws RemoteException Si ocurre un error en la llamada remota.
     */
    @Override
    public synchronized void logout(ChatClient client) throws RemoteException {
        // Borramos el cliente si lo encontramos en el mapa.
        clients.entrySet().removeIf(entry -> entry.getValue().equals(client));
        System.out.println("Cliente borrado de registro.");
    }

    /**
     * Publica un mensaje a todos los clientes conectados.
     *
     * @param msg Mensaje a publicar.
     * @throws RemoteException Si ocurre un error en la llamada remota.
     */
    @Override
    public void publish(ChatMessage msg) throws RemoteException {
        for (ChatClient client : clients.values()) {
            client.receive(msg);
        }
    }

    /**
     * Apaga el servidor de chat, desconectando a todos los clientes.
     *
     * @param client Cliente que solicita el apagado.
     * @throws RemoteException Si ocurre un error en la llamada remota.
     */
    @Override
    public void shutdown(ChatClient client) throws RemoteException {
        System.out.println("Servidor cerrando.");
        // TODO hay que limpiar algún registro de memoria???
        System.exit(0);
    }

}
