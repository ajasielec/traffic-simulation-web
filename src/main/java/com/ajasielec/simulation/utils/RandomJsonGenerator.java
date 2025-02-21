package com.ajasielec.simulation.utils;

import com.ajasielec.simulation.dto.Command;
import com.ajasielec.simulation.dto.CommandList;
import com.ajasielec.simulation.services.RandomCommandGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RandomJsonGenerator {
    public static void generateRandomJson(String filepath, int numberOfCommands) throws IOException {
        List<Command> commands = new ArrayList<>();

        for (int i = 0; i < numberOfCommands; i++) {
            commands.add(RandomCommandGenerator.generateRandomCommand());
        }

        CommandList commandList = CommandList.getInstance();
        commandList.setCommands(commands);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File(filepath), commandList);
    }
}
