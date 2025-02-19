package com.ajasielec.simulation.controllers;

import com.ajasielec.simulation.application.Simulation;
import com.ajasielec.simulation.utils.RandomJsonGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SimulationControllerTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private Simulation simulationMock;

    @InjectMocks
    private SimulationController controller;

    private final String resourcePath = "src/main/resources/";

    @BeforeEach
    void setUp() {

    }

    @Test
    void testStartSimulation_FileExists() throws Exception {
        String inputFile = "testInput.json";
        String outputFile = "testOutput.json";
        String inputPath = Paths.get(resourcePath, inputFile).toString();
        String outputPath = Paths.get(resourcePath, outputFile).toString();

        Path tempInputPath = Paths.get(inputPath);
        Files.createDirectories(tempInputPath.getParent());
        Files.writeString(tempInputPath, "{}");

        try (MockedStatic<Simulation> simulationMockedStatic = mockStatic(Simulation.class)) {
            simulationMockedStatic.when(() -> Simulation.getInstance(anyString(), anyString(), any()))
                    .thenReturn(simulationMock);
            doNothing().when(simulationMock).startSimulation();

            ResponseEntity<String> response = controller.startSimulation(inputFile, outputFile);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(response.getBody().contains("Simulation completed"));
            verify(messagingTemplate).convertAndSend(eq("/topic/status"), contains("Simulation started"));
            verify(simulationMock).startSimulation();
        } finally {
            Files.deleteIfExists(tempInputPath);
        }
    }

    @Test
    void testStartSimulation_FileNotFound() throws Exception {
        String inputFile = "nonExistentFile.json";
        String outputFile = "testOutput.json";

        ResponseEntity<String> response = controller.startSimulation(inputFile, outputFile);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().contains("Input file not found"));
        verify(messagingTemplate).convertAndSend(eq("/topic/status"), contains("Input file not found"));
        verifyNoMoreInteractions(simulationMock);
    }
}
