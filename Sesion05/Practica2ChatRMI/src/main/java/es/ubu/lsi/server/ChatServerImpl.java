package es.ubu.lsi.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Map;

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


public class ChatServerImpl implements ChatServer {

    /** Lista de clientes conectados: como en sockets usaré un mapa id-cliente.*/
    private ConcurrentHashMap<Integer, ChatClient> clients = new ConcurrentHashMap<>();
    /** Ids de los clientes */
    private int clientIdCounter = 1; // Contador para asignar ID únicos a cada cliente.

    /** Fecha de recepción de mensaje */
    private static SimpleDateFormat sdf = new SimpleDateFormat ("HH:mm:ss");

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
        // Iteramos sobre las entradas del mapa para encontrar el cliente y su ID correspondiente.
        Integer clientIdToRemove = null;
        for (Map.Entry<Integer, ChatClient> entry : clients.entrySet()) {
            if (entry.getValue().equals(client)) {
                clientIdToRemove = entry.getKey(); // Encontramos el ID del cliente.
                break; // Salimos del bucle una vez encontrado el cliente.
            }
        }

        // Si encontramos un cliente para remover, lo eliminamos y mostramos un mensaje personalizado.
        if (clientIdToRemove != null) {
            clients.remove(clientIdToRemove); // Eliminamos el cliente usando su ID.
            System.out.println("El cliente " + clientIdToRemove + " ha sido borrado de registro.");
        } else {
            System.out.println("Cliente no encontrado en el registro.");
        }
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
            if (client.getId() != msg.getId()) { // Restringimos que el remitente reciba el mensaje.
                client.receive(msg);
            }
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
        System.exit(0);
    }

    @Override
    public void dropUser(ChatClient requestingClient, String nicknameToDrop) throws RemoteException {
        // Se crea un stream de las entradas del mapa de clientes.
        Optional<Integer> userIdToDrop = clients.entrySet().stream()
                // Se aplica un filtro para buscar el usuario con el nickname dado.
                .filter(entry -> {
                    try {
                        // Se compara el nickname de cada cliente con el proporcionado.
                        return entry.getValue().getNickName().equalsIgnoreCase(nicknameToDrop);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                })
                // Se obtiene el ID del cliente
                .map(Map.Entry::getKey)
                // Se encuentra el primer elemento.
                .findFirst();

        try {
            // Comprueba si se encontró un ID de usuario.
            if (userIdToDrop.isPresent()) {
                // Si está presente, obtenemos el cliente y lo desconectamos mandandole un mensaje de logout
                ChatClient clientToDrop = clients.get(userIdToDrop.get());
                clientToDrop.receive(new ChatMessage(userIdToDrop.get(), "logout"));
                clients.remove(userIdToDrop.get());
                requestingClient.receive(new ChatMessage(0, "El usuario " + nicknameToDrop + " ha sido desconectado")); // Lo mandamos con id=0 que será el servidor.
            } else {
                // Si el ID de usuario no está presente, informamos que el usuario no existe.
                requestingClient.receive(new ChatMessage(0, "El usuario " + nicknameToDrop + " no existe"));
            }
        } catch (RuntimeException e) {
            throw (RemoteException) e.getCause();
        }
    }

}
