package com.ajasielec.simulation.dto;

import java.util.ArrayList;
import java.util.List;

public class StepStatus {
    private final List<String> leftVehicles;

    public StepStatus() {
        this.leftVehicles = new ArrayList<>();
    }

    public StepStatus(List<String> leftVehicles) {
        this.leftVehicles = List.copyOf(leftVehicles);
    }

    public List<String> getLeftVehicles() {
        return leftVehicles;
    }
}
