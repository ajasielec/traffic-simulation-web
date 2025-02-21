package com.ajasielec.simulation.models;

import com.ajasielec.simulation.enums.Direction;

public record Vehicle (String id, Direction startRoad, Direction endRoad) {
    public Vehicle {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Vehicle ID cannot be null or empty");
        }
        if (startRoad == endRoad) {
            throw new IllegalArgumentException("Start and end road cannot be the same");
        }
    }

    @Override
    public String toString() {
        return "Vehicle {id=" + id + ", startRoad=" + startRoad + ", endRoad=" + endRoad + "}";
    }
}
