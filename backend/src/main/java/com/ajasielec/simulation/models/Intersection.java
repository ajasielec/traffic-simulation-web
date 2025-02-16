package com.ajasielec.simulation.models;

import com.ajasielec.simulation.enums.LightColor;
import com.ajasielec.simulation.enums.TrafficCycle;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

public class Intersection {
    private static final Intersection instance = new Intersection();

    private final Queue<Vehicle> northQueue = new LinkedList<>();
    private final Queue<Vehicle> southQueue = new LinkedList<>();
    private final Queue<Vehicle> eastQueue = new LinkedList<>();
    private final Queue<Vehicle> westQueue = new LinkedList<>();

    private final TrafficLight northSouthLight = new TrafficLight(LightColor.RED, TrafficCycle.NORTH_SOUTH);
    private final TrafficLight eastWestLight = new TrafficLight(LightColor.GREEN, TrafficCycle.EAST_WEST);

    private Intersection() {}

    public static Intersection getInstance() {
        return instance;
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
            System.out.println(vehicle + " left intersection.");
            leftVehicles.add(vehicle.getId());
            TimeUnit.SECONDS.sleep(2);
        }
    }

    public void printQueues(){
        System.out.println("North queue: " + northQueue);
        System.out.println("South queue: " + southQueue);
        System.out.println("East queue: " + eastQueue);
        System.out.println("West queue: " + westQueue);
    }
}
