package com.ajasielec.simulation.utils;

import com.ajasielec.simulation.models.Command;
import com.ajasielec.simulation.models.CommandList;
import com.ajasielec.simulation.models.RandomCommandGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RandomJsonGenerator {
    public static void generateRandomJson(String filepath, int numberOfCommands) throws IOException {
        CommandList commandList = new CommandList();
        List<Command> commands = new ArrayList<Command>();

        for (int i = 0; i < numberOfCommands; i++) {
            commands.add(RandomCommandGenerator.generateRandomCommand());
        }

        commandList.setCommands(commands);

        // serialize and write to file
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File(filepath), commandList);
    }
}
