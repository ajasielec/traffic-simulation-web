package com.ajasielec.simulation.models;

import com.ajasielec.simulation.enums.LightColor;
import com.ajasielec.simulation.enums.TrafficCycle;

import java.util.concurrent.TimeUnit;

public class TrafficLight {
    private LightColor color;
    private final TrafficCycle cycle;

    public TrafficLight(LightColor color, TrafficCycle cycle) {
        this.color = color;
        this.cycle = cycle;
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
            printLightStatus();
            TimeUnit.SECONDS.sleep(2);
        }

        this.color = nextColor;
        printLightStatus();

        if (nextColor == LightColor.GREEN) {
            TimeUnit.SECONDS.sleep(3);
        } else {
            TimeUnit.SECONDS.sleep(1);
        }
    }

    @Override
    public String toString() {
        return "TrafficLight{" +
                "cycle='" + cycle + '\'' +
                ", currentColor=" + color + '}';
    }

    public void printLightStatus() {
        System.out.println("Light on " + cycle + ": " + color);
    }
}
