package com.ajasielec.simulation.utils;

import com.ajasielec.simulation.dto.CommandList;
import com.ajasielec.simulation.dto.SimulationResult;
import com.ajasielec.simulation.dto.StepStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonUtilsTest {
    @BeforeEach
    void resetSingleton() {
        CommandList.getInstance().setCommands(new ArrayList<>());
    }

    @Test
    void testDeserializeCommands() throws IOException {
        String inputFilePath = "src/test/resources/input.json";

        CommandList commandList = CommandList.getInstance();
        commandList.loadCommands(inputFilePath);

        assertNotNull(commandList);
        assertEquals(8, commandList.getCommands().size());
        assertEquals("addVehicle", commandList.getCommands().get(0).getType());
        assertEquals("vehicle1", commandList.getCommands().get(0).getVehicleId());
        assertEquals("south", commandList.getCommands().get(0).getStartRoad());
        assertEquals("north", commandList.getCommands().get(0).getEndRoad());
    }

    @Test
    void testSerializeResult() throws IOException {
        SimulationResult simulationResult = new SimulationResult();
        simulationResult.addStepStatus(new StepStatus(List.of("vehicle2", "vehicle1")));
        simulationResult.addStepStatus(new StepStatus(List.of()));
        simulationResult.addStepStatus(new StepStatus(List.of("vehicle3")));
        simulationResult.addStepStatus(new StepStatus(List.of("vehicle4")));

        String outputFilePath = "src/test/resources/output.json";

        JsonUtils.serializeResult(simulationResult, outputFilePath);

        File outputFile = new File(outputFilePath);
        assertTrue(outputFile.exists());

        ObjectMapper objectMapper = new ObjectMapper();
        SimulationResult deserializedResult = objectMapper.readValue(outputFile, SimulationResult.class);

        assertNotNull(deserializedResult);
        assertEquals(4, deserializedResult.getStepStatuses().size());

        assertEquals(List.of("vehicle2", "vehicle1"), deserializedResult.getStepStatuses().get(0).leftVehicles());
        assertEquals(List.of(), deserializedResult.getStepStatuses().get(1).leftVehicles());
        assertEquals(List.of("vehicle3"), deserializedResult.getStepStatuses().get(2).leftVehicles());
        assertEquals(List.of("vehicle4"), deserializedResult.getStepStatuses().get(3).leftVehicles());
    }
}