package com.ubu.sistdtr.proyectobase.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
/**
 * Configura los endpoints y el broker de mensajes de WebSocket.
 * Utiliza el protocolo STOMP para gestionar los mensajes en un formato que es fácil de usar y entender.
 *
 * Anotaciones:
 * @Configuration - Indica que la clase es parte de la configuración de Spring y puede contener definiciones de beans y configuraciones de Spring.
 * @EnableWebSocketMessageBroker - Habilita el manejo de mensajes WebSocket con un broker de mensajes respaldando el modelo basado en eventos.
 */

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Registra los endpoints de STOMP que los clientes usarán para conectarse al servidor WebSocket.
     *
     * @param registry el registro de puntos finales de STOMP donde se pueden registrar endpoints.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {// Registrar un endpoint STOMP en "/chat". Esto permite a los clientes conectar usando SockJS.
        registry.addEndpoint("/chat").withSockJS();
    }// Igual que hace Baeldung en el tutorial.

    /**
     * Configura el broker de mensajes, definiendo prefijos de destino de aplicación y habilitando un simple broker de mensajes.
     *
     * @param registry el registro del broker de mensajes para configurar.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");// Habilitar un simple broker de mensajes para mantener la transmisión de mensajes en sub-tópicos bajo "/topic".
        registry.setApplicationDestinationPrefixes("/app");// Definir prefijo para los destinos mapeados a métodos anotados con @MessageMapping.
    }
}
