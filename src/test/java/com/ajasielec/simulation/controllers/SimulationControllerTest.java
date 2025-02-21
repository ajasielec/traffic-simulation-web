package com.ajasielec.simulation.controllers;

import com.ajasielec.simulation.application.Simulation;
import com.ajasielec.simulation.utils.RandomJsonGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SimulationControllerTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private SimulationController controller;

    @Test
    void testStartSimulation_FileExists() throws Exception {
        String inputFile = "testInput.json";
        String outputFile = "testOutput.json";
        String resourcePath = "src/main/resources/";
        String inputPath = Paths.get(resourcePath, inputFile).toString();

        Path tempInputPath = Paths.get(inputPath);
        Files.createDirectories(tempInputPath.getParent());
        Files.writeString(tempInputPath, "{}");

        try {
            try (MockedConstruction<Simulation> mocked = mockConstruction(Simulation.class)) {
                ResponseEntity<String> response = controller.startSimulation(inputFile, outputFile);

                assertEquals(1, mocked.constructed().size());

                Simulation mockSimulation = mocked.constructed().get(0);

                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertTrue(Objects.requireNonNull(response.getBody()).contains("Simulation completed"));
                verify(messagingTemplate).convertAndSend(anyString(), contains("Starting a simulation..."));
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
        assertTrue(Objects.requireNonNull(response.getBody()).contains("Input file not found"));
        verify(messagingTemplate).convertAndSend(anyString(), contains("Input file not found"));
    }

    @Test
    void testStartRandomSimulation() throws IOException {
        try (MockedStatic<RandomJsonGenerator> generatorStatic = mockStatic(RandomJsonGenerator.class);
             MockedConstruction<Simulation> mocked = mockConstruction(Simulation.class)) {

            generatorStatic.when(() ->
                            RandomJsonGenerator.generateRandomJson(contains("randomInput.json"), eq(5)))
                    .thenAnswer(invocation -> null);

            String result = controller.startRandomSimulation(5);

            assertEquals(1, mocked.constructed().size());

            Simulation mockSimulation = mocked.constructed().get(0);

            assertTrue(result.contains("Simulation completed"));
            verify(messagingTemplate).convertAndSend(eq("/topic/status"), eq("Starting a simulation..."));

            generatorStatic.verify(() ->
                    RandomJsonGenerator.generateRandomJson(contains("randomInput.json"), eq(5)));

            verify(mockSimulation).startSimulation();
        }
    }

    @Test
    void testStartRandomSimulation_GeneratorThrowsException() {
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