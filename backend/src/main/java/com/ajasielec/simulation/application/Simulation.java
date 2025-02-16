package com.ajasielec.simulation.application;

import com.ajasielec.simulation.enums.Direction;
import com.ajasielec.simulation.models.*;
import com.ajasielec.simulation.utils.JsonUtils;

import java.io.IOException;
import java.util.List;

public class Simulation {
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

    public void startSimulation() {
        System.out.println("Starting simulation with input file: " + inputFile + " and output file: " + outputFile);

        try {
            Intersection intersection = Intersection.getInstance();
            SimulationResult simulationResult = new SimulationResult();
            CommandList commandList = CommandList.getInstance();
            commandList.loadCommands(inputFile);

            for (Command command : commandList.getCommands()) {
                executeCommand(command, intersection, simulationResult);
            }

            JsonUtils.serializeResult(simulationResult, outputFile);
            System.out.println("Simulation results saved to: " + outputFile);

        } catch (IOException e) {
            System.err.println("Error reading or writing files: " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid command or direction: " + e.getMessage());
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
                System.out.println("Added vehicle: " + vehicle + " on " + start + " road.");
                break;
            case "step":
                List<String> leftVehicles = intersection.step();
                simulationResult.addStepStatus(new StepStatus(leftVehicles));
                System.out.println("Step status (left vehicles): " + leftVehicles);
                break;
            default:
                System.out.println("Unknown command type: " + command.getType());
                break;
        }
    }
}
