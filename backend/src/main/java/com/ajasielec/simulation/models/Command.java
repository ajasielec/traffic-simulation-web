package com.ajasielec.simulation.models;

public class Command {
    private String type;
    private String vehicleId;
    private String startRoad;
    private String endRoad;

    public Command() {}

    public Command(String type) {
        this.type = type;
    }

    public Command(String type, String vehicleId, String startRoad, String endRoad) {
        this.type = type;
        this.vehicleId = vehicleId;
        this.startRoad = startRoad;
        this.endRoad = endRoad;
    }

    public String getType(){return type;}
    public String getVehicleId(){return vehicleId;}
    public String getStartRoad(){return startRoad;}
    public String getEndRoad(){return endRoad;}

    public void setType(String type){this.type = type;}
    public void setVehicleId(String vehicleId){this.vehicleId = vehicleId;}
    public void setStartRoad(String startRoad){this.startRoad = startRoad;}
    public void setEndRoad(String endRoad){this.endRoad = endRoad;}
}
