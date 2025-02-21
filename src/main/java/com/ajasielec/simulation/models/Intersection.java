package com.ajasielec.simulation.models;

import com.ajasielec.simulation.enums.LightColor;
import com.ajasielec.simulation.enums.TrafficCycle;
import com.ajasielec.simulation.services.AbstractMessageSender;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

public class Intersection extends AbstractMessageSender {
    private static boolean isTestMode = false;

    private final Queue<Vehicle> northQueue = new LinkedList<>();
    private final Queue<Vehicle> southQueue = new LinkedList<>();
    private final Queue<Vehicle> eastQueue = new LinkedList<>();
    private final Queue<Vehicle> westQueue = new LinkedList<>();

    private final TrafficLight northSouthLight = new TrafficLight(LightColor.RED, TrafficCycle.NORTH_SOUTH);
    private final TrafficLight eastWestLight = new TrafficLight(LightColor.GREEN, TrafficCycle.EAST_WEST);

    public Intersection() {}

    public Intersection(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
        northSouthLight.setMessagingTemplate(messagingTemplate);
        eastWestLight.setMessagingTemplate(messagingTemplate);
    }

    public void setTestMode(boolean testMode) {
        isTestMode = testMode;
    }

    @Override
    public void setMessagingTemplate(SimpMessagingTemplate messagingTemplate) {
        super.messagingTemplate = messagingTemplate;
        northSouthLight.setMessagingTemplate(messagingTemplate);
        eastWestLight.setMessagingTemplate(messagingTemplate);
    }

    public void addVehicle (Vehicle vehicle) {
        switch (vehicle.getStartRoad()) {
            case NORTH -> northQueue.add(vehicle);
            case SOUTH -> southQueue.add(vehicle);
            case EAST -> eastQueue.add(vehicle);
            case WEST -> westQueue.add(vehicle);
        }
    }

    public List<String> step() throws InterruptedException {
        List<String> leftVehicles = new ArrayList<>();

        if (shouldSwitchToNorthSouth()) {
            switchLights(northSouthLight, eastWestLight);
            processQueue(northQueue, leftVehicles);
            processQueue(southQueue, leftVehicles);
        } else {
            switchLights(eastWestLight, northSouthLight);
            processQueue(eastQueue, leftVehicles);
            processQueue(westQueue, leftVehicles);
        }
        return leftVehicles;
    }

    private boolean shouldSwitchToNorthSouth() {
        return northQueue.size() + southQueue.size() >= eastQueue.size() + westQueue.size();
    }

    private void switchLights(TrafficLight toGreen, TrafficLight toRed) throws InterruptedException {
        if (toGreen.getColor() == LightColor.RED) {
            toRed.setColor(LightColor.RED);
            toGreen.setColor(LightColor.GREEN);
        }
    }

    private void processQueue(Queue<Vehicle> queue, List<String> leftVehicles) throws InterruptedException {
        if (!queue.isEmpty()) {
            Vehicle vehicle = queue.poll();
            leftVehicles.add(vehicle.getId());
            if (!isTestMode){
                sendMessage(vehicle + " left intersection.");
                TimeUnit.SECONDS.sleep(2);
            }
        }
    }
}
