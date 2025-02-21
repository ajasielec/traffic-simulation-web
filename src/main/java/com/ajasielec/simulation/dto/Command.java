package com.ajasielec.simulation.dto;

public class Command {
    private final String type;
    private final String vehicleId;
    private final String startRoad;
    private final String endRoad;

    public Command() {
        this.type = null;
        this.vehicleId = null;
        this.startRoad = null;
        this.endRoad = null;
    }

    public Command(String type) {
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Command type cannot be null or empty.");
        }
        this.type = type;
        this.vehicleId = null;
        this.startRoad = null;
        this.endRoad = null;
    }

    public Command(String type, String vehicleId, String startRoad, String endRoad) {
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Command type cannot be null or empty.");
        }
        this.type = type;
        this.vehicleId = vehicleId;
        this.startRoad = startRoad;
        this.endRoad = endRoad;
    }

    public String getType(){return type;}
    public String getVehicleId(){return vehicleId;}
    public String getStartRoad(){return startRoad;}
    public String getEndRoad(){return endRoad;}
}
