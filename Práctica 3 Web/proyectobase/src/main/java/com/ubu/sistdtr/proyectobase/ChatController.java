package com.ubu.sistdtr.proyectobase;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public String processMessage(String message) {
        return message;
    }
}
