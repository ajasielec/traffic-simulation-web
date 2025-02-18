package com.ajasielec.simulation.application;

import com.ajasielec.simulation.enums.Direction;
import com.ajasielec.simulation.models.*;
import com.ajasielec.simulation.utils.JsonUtils;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.io.IOException;
import java.util.List;

public class Simulation extends AbstractMessageSender {
    private static Simulation instance;
    private final String inputFile;
    private final String outputFile;

    private Simulation(String inputFile, String outputFile) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
    }

    public static synchronized Simulation getInstance(String inputFile, String outputFile) {
        if (instance == null) {
            instance = new Simulation(inputFile, outputFile);
        }
        return instance;
    }

    public static synchronized Simulation getInstance(String inputFile, String outputFile, SimpMessagingTemplate messagingTemplate) {
        if (instance == null) {
            instance = new Simulation(inputFile, outputFile);
        }
        instance.setMessagingTemplate(messagingTemplate);
        return instance;
    }

    public void startSimulation() {
        try {
            Intersection intersection = Intersection.getInstance();
            intersection.setMessagingTemplate(messagingTemplate);
            SimulationResult simulationResult = new SimulationResult();
            CommandList commandList = CommandList.getInstance();
            commandList.loadCommands(inputFile);

            for (Command command : commandList.getCommands()) {
                executeCommand(command, intersection, simulationResult);
            }

            JsonUtils.serializeResult(simulationResult, outputFile);
            System.out.println("Simulation results saved to: " + outputFile);

        } catch (IOException e) {
            sendMessage("Error reading or writing files: " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            sendMessage("Invalid command or direction: " + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void executeCommand(Command command, Intersection intersection, SimulationResult simulationResult) throws InterruptedException {
        switch (command.getType()) {
            case "addVehicle":
                Direction start = Direction.valueOf(command.getStartRoad().toUpperCase());
                Direction end = Direction.valueOf(command.getEndRoad().toUpperCase());
                Vehicle vehicle = new Vehicle(command.getVehicleId(), start, end);
                intersection.addVehicle(vehicle);
                sendMessage("Added vehicle: " + vehicle + " on " + start + " road.");
                break;
            case "step":
                List<String> leftVehicles = intersection.step();
                simulationResult.addStepStatus(new StepStatus(leftVehicles));
                sendMessage("Step status (left vehicles): " + leftVehicles);
                break;
            default:
                sendMessage("Unknown command type: " + command.getType());
                break;
        }
    }
}
