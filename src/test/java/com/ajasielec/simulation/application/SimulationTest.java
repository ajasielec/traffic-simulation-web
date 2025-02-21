package com.ajasielec.simulation.application;

import com.ajasielec.simulation.enums.Direction;
import com.ajasielec.simulation.models.Intersection;
import com.ajasielec.simulation.models.TrafficLight;
import com.ajasielec.simulation.models.Vehicle;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimulationTest {
    @Test
    void testSimulationSteps() throws InterruptedException {
        TrafficLight light = new TrafficLight();
        light.setTestMode(true);

        Intersection intersection = new Intersection();
        intersection.setTestMode(true);

        intersection.addVehicle(new Vehicle("vehicle1", Direction.SOUTH, Direction.NORTH));
        intersection.addVehicle(new Vehicle("vehicle2", Direction.NORTH, Direction.EAST));

        List<String> step1Result = intersection.step();
        assertEquals(List.of("vehicle2", "vehicle1"), step1Result);

        List<String> step2Result = intersection.step();
        assertEquals(List.of(), step2Result);

        intersection.addVehicle(new Vehicle("vehicle3", Direction.WEST, Direction.EAST));
        intersection.addVehicle(new Vehicle("vehicle4", Direction.WEST, Direction.NORTH));

        List<String> step3Result = intersection.step();
        assertEquals(List.of("vehicle3"), step3Result);

        List<String> step4Result = intersection.step();
        assertEquals(List.of("vehicle4"), step4Result);
    }
}
