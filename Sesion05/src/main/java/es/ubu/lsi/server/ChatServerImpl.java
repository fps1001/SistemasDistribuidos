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

    @Override
    public synchronized int checkIn(ChatClient client) throws RemoteException {
        int clientId = clientIdCounter++;
        clients.put(clientId, client); // añado a mapa el cliente con su id.
        System.out.println("Cliente registrado con ID: " + clientId);
        return clientId;
    }

    @Override
    public synchronized void logout(ChatClient client) throws RemoteException {
        // Borramos el cliente si lo encontramos en el mapa.
        clients.entrySet().removeIf(entry -> entry.getValue().equals(client));
        System.out.println("Cliente borrado de registro.");
    }

    @Override
    public void publish(ChatMessage msg) throws RemoteException {
        for (ChatClient client : clients.values()) {
            client.receive(msg);
        }
    }

    @Override
    public void shutdown(ChatClient client) throws RemoteException {
        System.out.println("Servidor cerrando.");
        // TODO hay que limpiar algún registro de memoria???
        System.exit(0);
    }

}
