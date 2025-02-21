package com.ajasielec.simulation.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.logging.Logger;

@Controller
@CrossOrigin(origins = {"http://localhost:63342"})
public class WebSocketController {
    private static final Logger LOGGER = Logger.getLogger(WebSocketController.class.getName());

    @MessageMapping("/simulation")
    @SendTo("/topic/status")
    public String sendSimulationStatus(@Payload String message) {
        LOGGER.info("Received WebSocket message: " + message);
        return "Simulation status: " + message;
    }
}
