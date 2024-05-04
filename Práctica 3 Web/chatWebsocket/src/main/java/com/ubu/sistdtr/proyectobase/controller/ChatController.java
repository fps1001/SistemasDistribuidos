package com.ubu.sistdtr.proyectobase.controller;

import com.ubu.sistdtr.proyectobase.model.Message;
import com.ubu.sistdtr.proyectobase.user.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para manejar la funcionalidad de chat en tiempo real.
 * Proporciona endpoints para enviar y recibir mensajes, además de obtener información del usuario.
 */
@Controller
public class ChatController {

    /**
     * Función de envío de mensaje. Obtendrá los datos del usuario actual y
     * generará un mensaje completo tomando los datos del csv.
     * */

    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public Message processMessage(@Payload Message mensaje, SimpMessageHeaderAccessor headerAccessor) {
        // Obtiene el objeto Authentication que contiene los detalles del usuario actualmente autenticado.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Verifica si hay un usuario autenticado y guarda sus datos en el mensaje.
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            mensaje.setFrom(userDetails.getUsername());
            mensaje.setFrom_id(userDetails.getId());
            mensaje.setFrom_level(userDetails.getUserLevel());
        }
        return mensaje;
    }

    /**
     * Endpoint HTTP GET para obtener información del usuario autenticado.
     * Devuelve datos del usuario si está autenticado, de lo contrario devuelve un estado HTTP 401.
     *
     * @param authentication Objeto que representa la autenticación del usuario en la sesión.
     * @return ResponseEntity que contiene información del usuario o un estado de error si no está autenticado.
     */
    @GetMapping("/api/userinfo")
    public ResponseEntity<Map<String, Object>> getUserInfo(Authentication authentication) {
        // Verifica si el usuario está autenticado.
        if (authentication != null && authentication.isAuthenticated()) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            // Crea un mapa para almacenar información del usuario.
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("username", userDetails.getUsername());
            userInfo.put("id", userDetails.getId());
            userInfo.put("level", userDetails.getUserLevel().getLevelValue());
            userInfo.put("isInclusive", userDetails.isInclusive());

            return ResponseEntity.ok(userInfo); // Retorna la información del usuario en formato ResponseEntity.
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); //401 si no autenticado.
    }

}
