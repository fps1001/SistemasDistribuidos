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

    public static void main(String[] args) {
        try {
            //* Establecer la política de seguridad
            //System.setProperty("java.security.policy", "path/to/security.policy");

//            if (System.getSecurityManager() == null) {
//                System.setSecurityManager(new SecurityManager());
//            }

            //* Crea e instancia el servidor
            ChatServerImpl server = new ChatServerImpl();
            // Los stubs son la puerta de entrada para el lado del cliente
            ChatServer stub = (ChatServer) UnicastRemoteObject.exportObject(server, 0);

            // Iniciar el registro RMI en el puerto especificado o en el puerto por defecto
//            int port = args.length > 0 ? Integer.parseInt(args[0]) : 1099;
//            LocateRegistry.createRegistry(port);
//            Registry registry = LocateRegistry.getRegistry(port);
            // Liga el resguardo de objeto remoto en el registro
            Registry registry = LocateRegistry.getRegistry();

            // Registra el servidor en el registro RMI con un nombre único
            String name = "ChatServer";
            registry.rebind(name, stub);

            System.out.println("Servidor de chat RMI iniciado y registrado con el nombre: " + name);
        } catch (RemoteException e) {
            System.err.println("Excepción al iniciar el servidor de chat: " + e.toString());
            e.printStackTrace();
        }
    }
}
