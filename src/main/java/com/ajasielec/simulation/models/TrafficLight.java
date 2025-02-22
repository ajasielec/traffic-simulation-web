package com.ajasielec.simulation.models;

import com.ajasielec.simulation.enums.LightColor;
import com.ajasielec.simulation.enums.TrafficCycle;
import com.ajasielec.simulation.services.AbstractMessageSender;

import java.util.concurrent.TimeUnit;

public class TrafficLight extends AbstractMessageSender {
    private LightColor color;
    private TrafficCycle cycle;
    private static boolean isTestMode = false;
    private String emoji = "";

    public TrafficLight() {
    }

    public TrafficLight(LightColor color, TrafficCycle cycle) {
        this.color = color;
        this.cycle = cycle;
    }

    public void setTestMode(boolean isTestMode) {
        TrafficLight.isTestMode = isTestMode;
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
        if (this.color == nextColor) return;

        if (this.color != LightColor.YELLOW) {
            setLightAndSleep(LightColor.YELLOW, 2);
        }

        setLightAndSleep(nextColor, nextColor == LightColor.GREEN ? 3 : 1);
    }

    private void setLightAndSleep(LightColor newColor, int sleepTime) throws InterruptedException {
        this.color = newColor;
        if (!isTestMode) {
            if (messagingTemplate != null) { emoji = "🚦 ";}
            sendMessage(String.format(emoji + "Traffic lights on %s changed to %s.", cycle, newColor));
            TimeUnit.SECONDS.sleep(sleepTime);
        }
    }
}
