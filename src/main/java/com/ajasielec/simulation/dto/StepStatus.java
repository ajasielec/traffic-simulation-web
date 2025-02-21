package com.ajasielec.simulation.dto;

import java.util.List;

public record StepStatus(List<String> leftVehicles) {
    public StepStatus(List<String> leftVehicles) {
        this.leftVehicles = leftVehicles != null ? List.copyOf(leftVehicles) : List.of();
    }
}
