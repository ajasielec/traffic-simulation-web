package com.ajasielec.simulation.models;

import com.ajasielec.simulation.enums.LightColor;
import com.ajasielec.simulation.enums.TrafficCycle;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.concurrent.TimeUnit;

public class TrafficLight {
    private LightColor color;
    private TrafficCycle cycle;
    private static boolean isTestMode = false;
    private SimpMessagingTemplate messagingTemplate;

    public TrafficLight() {
    }

    public TrafficLight(LightColor color, TrafficCycle cycle) {
        this.color = color;
        this.cycle = cycle;
    }

    public void setTestMode(boolean isTestMode) {
        this.isTestMode = isTestMode;
    }

    public void setMessagingTemplate(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    private void sendMessage(String message) {
        if (messagingTemplate != null) {
            messagingTemplate.convertAndSend("/topic/status", message);
        }
        System.out.println(message);
    }

    public LightColor getColor() {
        return color;
    }

    public void setColor(LightColor nextColor) throws InterruptedException {
        if (this.color == nextColor) return;

        if (nextColor == LightColor.GREEN || nextColor == LightColor.RED) {
            transitionTo(nextColor);
        }
    }

    private void transitionTo(LightColor nextColor) throws InterruptedException {
        if (this.color != LightColor.YELLOW) {
            this.color = LightColor.YELLOW;
            if (!isTestMode) {
                printLightStatus();
                TimeUnit.SECONDS.sleep(2);
            }
        }

        this.color = nextColor;
        if (!isTestMode) {
            printLightStatus();
        }

        if (nextColor == LightColor.GREEN) {
            if (!isTestMode) {
                TimeUnit.SECONDS.sleep(3);
            }
        } else {
            if (!isTestMode) {
                TimeUnit.SECONDS.sleep(1);
            }
        }
    }

    @Override
    public String toString() {
        return "TrafficLight{" +
                "cycle='" + cycle + '\'' +
                ", currentColor=" + color + '}';
    }

    public void printLightStatus() {
        sendMessage("Light on " + cycle + ": " + color);
    }
}
