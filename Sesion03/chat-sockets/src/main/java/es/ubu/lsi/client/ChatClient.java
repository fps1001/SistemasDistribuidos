package es.ubu.lsi.client;

import es.ubu.lsi.common.ChatMessage;

/**
 * Interfaz para la funcionalidad del cliente en el sistema de chat.
 * Define la firma para los métodos de envío de mensajes, desconexión y arranque del cliente.
 *
 * @author Fernando Pisot Serrano
 * @email fps1001@alu.ubu.es
 */
public interface ChatClient {
    /**
     * Envia un mensaje al servidor de chat.
     *
     * @param message mensaje a enviar.
     */
    void sendMessage(ChatMessage message);

    /**
     * Desconecta al cliente del servidor de chat.
     */
    void disconnect();

    /**
     * Inicia el proceso del cliente.
     */
    void start();
}
