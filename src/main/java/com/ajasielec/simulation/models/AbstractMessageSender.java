package com.ajasielec.simulation.models;

import org.springframework.messaging.simp.SimpMessagingTemplate;

public class AbstractMessageSender implements MessageSender {
    protected SimpMessagingTemplate messagingTemplate;

    public void setMessagingTemplate(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void sendMessage(String message) {
        if (messagingTemplate != null) {
            messagingTemplate.convertAndSend("/topic/status", message);
        }
        System.out.println(message);
    }
}
