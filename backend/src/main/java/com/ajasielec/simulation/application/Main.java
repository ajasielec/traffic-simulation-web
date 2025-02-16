package com.ajasielec.simulation.application;

import java.io.IOException;
import java.nio.file.Paths;

public class Main {
    private static final String DEFAULT_INPUT_FILE = "randomInput.json";
    private static final String DEFAULT_OUTPUT_FILE = "randomOutput.json";
    private static final String RESOURCE_PATH = "src/main/resources/";

    public static void main(String[] args) throws IOException {
        String inputFile = getInputFile(args);
        String outputFile = getOutputFile(args);

        String inputPath = buildPath(inputFile);
        String outputPath = buildPath(outputFile);

        if (inputFile.equals(DEFAULT_INPUT_FILE)) {
            System.out.println("Too few arguments provided. Starting random simulation.");
            return;
            // RandomJsonGenerator.generateRandomJson(inputPath, 10);
        }

        // start the simulation
        Simulation simulation = Simulation.getInstance(inputPath, outputPath);
        simulation.startSimulation();
    }

    private static String getInputFile(String[] args) {
        if (args.length >= 2) {
            return args[0];
        }
        return DEFAULT_INPUT_FILE;
    }

    private static String getOutputFile(String[] args) {
        if (args.length >= 2) {
            return args[1];
        }
        return DEFAULT_OUTPUT_FILE;
    }

    private static String buildPath(String fileName) {
        return Paths.get(RESOURCE_PATH, fileName).toString();
    }
}
