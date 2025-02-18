package com.ajasielec.simulation.utils;

import com.ajasielec.simulation.models.CommandList;
import com.ajasielec.simulation.models.SimulationResult;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JsonUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static CommandList deserializeCommands (String filename) throws IOException {
        return objectMapper.readValue(new File(filename), CommandList.class);
    }

    public static void serializeResult (SimulationResult simulationResult, String outputPath) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File (outputPath), simulationResult);
    }
}
