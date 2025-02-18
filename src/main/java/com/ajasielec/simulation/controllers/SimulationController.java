package com.ajasielec.simulation.controllers;

import com.ajasielec.simulation.application.Simulation;
import com.ajasielec.simulation.models.SimulationResult;
import com.ajasielec.simulation.utils.RandomJsonGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Paths;

@RestController
public class SimulationController {
    private static final String RESOURCE_PATH = "src/main/resources/";

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping("/start")
    public String startSimulation(@RequestParam String inputFile, @RequestParam String outputFile) {
        String inputPath = Paths.get(RESOURCE_PATH, inputFile).toString();
        String outputPath = Paths.get(RESOURCE_PATH, outputFile).toString();

        messagingTemplate.convertAndSend("/topic/status", "Simulation started with " + inputPath);

        Simulation simulation = Simulation.getInstance(inputPath, outputPath, messagingTemplate);
        // simulation.setMessagingTemplate(messagingTemplate);
        simulation.startSimulation();

        // messagingTemplate.convertAndSend("/topic/status", "Simulation completed! Results save to " + outputPath);

        return "Simulation completed! Results save to " + outputPath;
    }

    @PostMapping("/startrandom")
    public String startRandomSimulation() throws IOException {
        String inputPath = Paths.get(RESOURCE_PATH, "randomInput.json").toString();
        String outputPath = Paths.get(RESOURCE_PATH, "randomOutput.json").toString();

        RandomJsonGenerator.generateRandomJson(inputPath, 10);

        messagingTemplate.convertAndSend("/topic/status", "Simulation started with random JSON");

        Simulation simulation = Simulation.getInstance(inputPath, outputPath, messagingTemplate);
        simulation.startSimulation();

        return "Simulation completed! Results save to " + outputPath;
    }

}
