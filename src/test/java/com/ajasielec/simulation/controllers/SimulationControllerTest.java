package com.ajasielec.simulation.controllers;

import com.ajasielec.simulation.application.Simulation;
import com.ajasielec.simulation.utils.RandomJsonGenerator;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SimulationControllerTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private SimulationController controller;

    private final String resourcePath = "src/main/resources/";

    @Test
    void testStartSimulation_FileExists() throws Exception {
        String inputFile = "testInput.json";
        String outputFile = "testOutput.json";
        String inputPath = Paths.get(resourcePath, inputFile).toString();

        Path tempInputPath = Paths.get(inputPath);
        Files.createDirectories(tempInputPath.getParent());
        Files.writeString(tempInputPath, "{}");

        try {
            Simulation mockSimulation = mock(Simulation.class);

            try (MockedStatic<Simulation> simulationStatic = mockStatic(Simulation.class)) {
                simulationStatic.when(() ->
                                Simulation.getInstance(anyString(), anyString(), any(SimpMessagingTemplate.class)))
                        .thenReturn(mockSimulation);

                ResponseEntity<String> response = controller.startSimulation(inputFile, outputFile);

                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertTrue(response.getBody().contains("Simulation completed"));
                verify(messagingTemplate).convertAndSend(anyString(), contains("Simulation started"));
                verify(mockSimulation).startSimulation();
            }
        } finally {
            Files.deleteIfExists(tempInputPath);
        }
    }

    @Test
    void testStartSimulation_FileNotFound() {
        String inputFile = "nonExistentFile.json";
        String outputFile = "testOutput.json";

        ResponseEntity<String> response = controller.startSimulation(inputFile, outputFile);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().contains("Input file not found"));
        verify(messagingTemplate).convertAndSend(anyString(), contains("Input file not found"));
    }

    @Test
    void testStartRandomSimulation() throws IOException {
        try (MockedStatic<RandomJsonGenerator> generatorStatic = mockStatic(RandomJsonGenerator.class);
             MockedStatic<Simulation> simulationStatic = mockStatic(Simulation.class)) {

            Simulation mockSimulation = mock(Simulation.class);
            simulationStatic.when(() ->
                            Simulation.getInstance(anyString(), anyString(), any(SimpMessagingTemplate.class)))
                    .thenReturn(mockSimulation);

            String result = controller.startRandomSimulation(5);

            assertTrue(result.contains("Simulation completed"));
            verify(messagingTemplate).convertAndSend(eq("/topic/status"), eq("Simulation started with random JSON"));

            generatorStatic.verify(() ->
                    RandomJsonGenerator.generateRandomJson(contains("randomInput.json"), eq(5)));

            verify(mockSimulation).startSimulation();
        }
    }

    @Test
    void testStartRandomSimulation_GeneratorThrowsException() throws IOException {
        try (MockedStatic<RandomJsonGenerator> generatorStatic = mockStatic(RandomJsonGenerator.class)) {
            generatorStatic.when(() -> RandomJsonGenerator.generateRandomJson(anyString(), anyInt()))
                    .thenThrow(new IOException("Test IO exception"));

            Exception exception = assertThrows(IOException.class, () ->
                    controller.startRandomSimulation(5));

            assertEquals("Test IO exception", exception.getMessage());
            verify(messagingTemplate, never()).convertAndSend(anyString(), anyString());
        }
    }
}