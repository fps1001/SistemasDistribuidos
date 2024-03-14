package es.ubu.lsi.server;

import es.ubu.lsi.common.ChatMessage;

/**
 * Interfaz para la funcionalidad del servidor en el sistema de chat.
 * Define los métodos para el arranque del servidor, la multidifusión de mensajes, la eliminación de clientes y el apagado.
 *
 * @author Fernando Pisot Serrano
 * @email fps1001@alu.ubu.es
 */
public interface ChatServer {
    /**
     * Inicia el servidor y espera las conexiones de los clientes.
     */
    void startup();

    /**
     * Envía un mensaje a todos los clientes conectados.
     *
     * @param msg el mensaje a enviar.
     */
    //void broadcast(String msg);

    void broadcast(ChatMessage msg);

    /**
     * Elimina un cliente del chat.
     *
     * @param id identificador del cliente a eliminar.
     */
    void remove(int id);

    /**
     * Cierra todas las conexiones y apaga el servidor.
     */
    void shutdown();
}
