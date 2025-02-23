package com.ajasielec.simulation.application;

import com.ajasielec.simulation.dto.Command;
import com.ajasielec.simulation.dto.CommandList;
import com.ajasielec.simulation.dto.SimulationResult;
import com.ajasielec.simulation.dto.StepStatus;
import com.ajasielec.simulation.enums.Direction;
import com.ajasielec.simulation.models.*;
import com.ajasielec.simulation.services.AbstractMessageSender;
import com.ajasielec.simulation.utils.JsonUtils;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Simulation extends AbstractMessageSender {
    private static final Logger LOGGER = Logger.getLogger(Simulation.class.getName());
    private final String inputFile;
    private final String outputFile;
    private final Intersection intersection;
    String emoji = "";

    public Simulation(String inputFile, String outputFile) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.intersection = new Intersection();
    }

    public Simulation(String inputFile, String outputFile, SimpMessagingTemplate messagingTemplate) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.messagingTemplate = messagingTemplate;
        this.intersection = new Intersection();
        this.intersection.setMessagingTemplate(messagingTemplate);
    }


    public void startSimulation() {
        try {
            if (messagingTemplate != null) { emoji = "🚀 ";}
            sendMessage(emoji + "Simulation started with " + inputFile);
            SimulationResult simulationResult = new SimulationResult();
            CommandList commandList = CommandList.getInstance();
            commandList.loadCommands(inputFile);

            for (Command command : commandList.getCommands()) {
                executeCommand(command, simulationResult);
            }

            JsonUtils.serializeResult(simulationResult, outputFile);
            if (messagingTemplate != null) { emoji = "💾 ";}
            sendMessage(emoji + "Simulation results saved to: " + outputFile);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading or writing files: ", e);
            sendMessage("Error reading or writing files: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "Invalid command or direction: ", e);
            sendMessage("Invalid input: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.log(Level.SEVERE, "Simulation interrupted: ", e);
            sendMessage("Simulation was interrupted.");
        }
    }

    private void executeCommand(Command command, SimulationResult simulationResult) throws InterruptedException {
        switch (command.getType()) {
            case "addVehicle":
                addVehicle(command);
                break;
            case "step":
                step(simulationResult);
                break;
            default:
                sendMessage("Unknown command type: " + command.getType());
        }
    }

    private void addVehicle(Command command) {
        Direction start = Direction.valueOf(command.getStartRoad().toUpperCase());
        Direction end = Direction.valueOf(command.getEndRoad().toUpperCase());
        Vehicle vehicle = new Vehicle(command.getVehicleId(), start, end);
        intersection.addVehicle(vehicle);
        if (messagingTemplate != null) { emoji = "➕🚗 ";}
        sendMessage(String.format(emoji + "Vehicle %s added on %s road, heading to %s.", vehicle.getId(), start, end));
    }

    private void step(SimulationResult simulationResult) throws InterruptedException {
        List<String> leftVehicles = intersection.step();
        simulationResult.addStepStatus(new StepStatus(leftVehicles));
        if (messagingTemplate != null) { emoji = "✅ ";}
        sendMessage(String.format(emoji + "Step completed. Vehicles left the intersection: %s", leftVehicles.isEmpty() ? "None" : String.join(", ", leftVehicles)));
    }
}
