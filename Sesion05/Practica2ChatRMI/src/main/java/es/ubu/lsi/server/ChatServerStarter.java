package es.ubu.lsi.server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * ChatServerStarter: Inicia la comunicación con el cliente.
 *
 * <p>
 * Para más información sobre el proyecto, consultar el repositorio de GitHub.
 * Contacto: fps1001@alu.ubu.es
 *
 * @see <a href="https://github.com/fps1001/SistemasDistribuidos">Repositorio de GitHub</a>
 * @author Fernando Pisot Serrano
 */

public class ChatServerStarter {
    public ChatServerStarter() {
        start();
    }

    /**
     * Inicia el proceso de exportación del servidor remoto y registra RMI (rmiregistry)
     */
    public void start() {
        try { // versión simplificada para ver si arranca
            // Crea servidor
            ChatServerImpl server = new ChatServerImpl();
            // Y lo exporta:
            /* Los stubs son la puerta de entrada para el lado del cliente, construyen un bloque de información y
             envían esta información al lado del servidor.
             El bloque de información puede consistir en el nombre del método que se va a invocar,
             los parámetros para la JVM remota y el identificador del objeto remoto a utilizar.*/
            ChatServer stub = (ChatServer) UnicastRemoteObject.exportObject(server, 0);

            // Registra el objeto remoto en el servicio de nombres RMI Registry
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("/servidor", stub); // cambio a rebind para que no de fallo si tiene que registrar ya existente, con bind daría error.
            System.out.println("Servidor de chat RMI iniciado correctamente.");

        } catch (RemoteException e) {
            System.err.println("Excepción al iniciar el servidor de chat: " + e.toString());
            e.printStackTrace();
        }


    }
}
