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
    private boolean isTestMode = false;

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
        switch (vehicle.startRoad()) {
            case NORTH -> northQueue.add(vehicle);
            case SOUTH -> southQueue.add(vehicle);
            case EAST -> eastQueue.add(vehicle);
            case WEST -> westQueue.add(vehicle);
        }
    }

    public List<String> step() throws InterruptedException {
        List<String> leftVehicles = new ArrayList<>();
        boolean isNorthSouth = shouldSwitchToNorthSouth();

        switchLights(isNorthSouth ? northSouthLight : eastWestLight,
                isNorthSouth ? eastWestLight : northSouthLight);

        processQueue(isNorthSouth ? northQueue : eastQueue, leftVehicles);
        processQueue(isNorthSouth ? southQueue : westQueue, leftVehicles);

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
        Vehicle vehicle = queue.poll();
        if (vehicle != null) {
            leftVehicles.add(vehicle.id());
            if (!isTestMode){
                sendMessage(vehicle + " left intersection.");
                TimeUnit.SECONDS.sleep(2);
            }
        }
    }
}
