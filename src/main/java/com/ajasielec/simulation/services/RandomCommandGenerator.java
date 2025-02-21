package com.ajasielec.simulation.services;

import com.ajasielec.simulation.dto.Command;

import java.util.Random;

public class RandomCommandGenerator {
    private static final String[] ROAD_NAMES = {"north", "south", "east", "west"};
    private static int vehicleIdCounter = 1;
    private static final Random random = new Random();

    public static Command generateRandomCommand() {
        int commandTypeIndex = random.nextInt(2);

        if (commandTypeIndex == 0) {
            // "addVehicle" command
            String vehicleId = "vehicle" + vehicleIdCounter++;
            String startRoad = ROAD_NAMES[random.nextInt(ROAD_NAMES.length)];
            String endRoad = ROAD_NAMES[random.nextInt(ROAD_NAMES.length)];

            while (endRoad.equals(startRoad)) {
                endRoad = ROAD_NAMES[random.nextInt(ROAD_NAMES.length)];
            }

            return new Command("addVehicle", vehicleId, startRoad, endRoad);
        } else {
            // "step" command
            return new Command("step");
        }
    }
}
