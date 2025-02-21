package com.ajasielec.simulation.controllers;

import com.ajasielec.simulation.application.Simulation;
import com.ajasielec.simulation.utils.RandomJsonGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
public class SimulationController {
    private static final String RESOURCE_PATH = "src/main/resources/";

    private final SimpMessagingTemplate messagingTemplate;

    public SimulationController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/start")
    public ResponseEntity<String> startSimulation(@RequestParam String inputFile, @RequestParam String outputFile) {
        String inputPath = Paths.get(RESOURCE_PATH, inputFile).toString();
        String outputPath = Paths.get(RESOURCE_PATH, outputFile).toString();

        if (!Files.exists(Paths.get(inputPath))) {
            String errorMessage = "Input file not found: " + inputPath;
            messagingTemplate.convertAndSend("/topic/status", errorMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }

        try {
            Simulation simulation = new Simulation(inputPath, outputPath, messagingTemplate);
            simulation.startSimulation();
            messagingTemplate.convertAndSend("/topic/status", "\nSimulation completed!\n");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Simulation failed: " + e.getMessage());
        }

        String successMessage = "Simulation completed!\n";

        return ResponseEntity.ok(successMessage);
    }

    @PostMapping("/start-random")
    public String startRandomSimulation(@RequestParam int numberOfCommands) throws IOException {
        String inputPath = Paths.get(RESOURCE_PATH, "randomInput.json").toString();
        String outputPath = Paths.get(RESOURCE_PATH, "randomOutput.json").toString();

        RandomJsonGenerator.generateRandomJson(inputPath, numberOfCommands);

        Simulation simulation = new Simulation(inputPath, outputPath, messagingTemplate);
        simulation.startSimulation();

        messagingTemplate.convertAndSend("/topic/status", "\nSimulation completed!\n");

        return "Simulation completed!\n";
    }
}
