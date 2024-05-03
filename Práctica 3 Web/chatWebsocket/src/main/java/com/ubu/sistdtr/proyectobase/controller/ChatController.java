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

@Controller
public class ChatController {

    /**
     * Función de envío de mensaje. Obtendrá los datos del usuario actual y
     * generará un mensaje completo tomando los datos del csv.
     * */

    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public Message processMessage(@Payload Message mensaje, SimpMessageHeaderAccessor headerAccessor) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            mensaje.setFrom(userDetails.getUsername());
            mensaje.setFrom_id(userDetails.getId());
            mensaje.setFrom_level(userDetails.getUserLevel());
        }
        return mensaje;
    }


    @GetMapping("/api/userinfo")
    public ResponseEntity<Map<String, Object>> getUserInfo(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("username", userDetails.getUsername());
            userInfo.put("id", userDetails.getId());
            userInfo.put("level", userDetails.getUserLevel().getLevelValue());
            userInfo.put("isInclusive", userDetails.isInclusive());

            return ResponseEntity.ok(userInfo);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

//    @GetMapping("/api/userinfo")
//    public ResponseEntity<UserDetails> getUserInfo(Authentication authentication) {
//        if (authentication != null && authentication.isAuthenticated()) {
//            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//            return ResponseEntity.ok(userDetails);
//        }
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//    }



}
