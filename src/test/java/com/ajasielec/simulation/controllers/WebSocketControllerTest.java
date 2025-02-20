package com.ajasielec.simulation.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class WebSocketControllerTest {

    @Autowired
    private WebSocketController controller;

    @Test
    public void testControllerDirectly() {
        String result = controller.sendSimulationStatus("running");
        assertEquals("Simulation status: running", result);
    }

    @Test
    public void testMessageWithDifferentInput() {
        String result = controller.sendSimulationStatus("stopped");
        assertEquals("Simulation status: stopped", result);
    }

    @Test
    public void testMessageWithEmptyInput() {
        String result = controller.sendSimulationStatus("");
        assertEquals("Simulation status: ", result);
    }
}