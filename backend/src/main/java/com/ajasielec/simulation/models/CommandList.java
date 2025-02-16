package com.ajasielec.simulation.models;

import com.ajasielec.simulation.utils.JsonUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommandList {
    private static CommandList instance;
    private List<Command> commands;

    public CommandList() {
        commands = new ArrayList<>();
    }

    public static synchronized CommandList getInstance() {
        if (instance == null) {
            instance = new CommandList();
        }
        return instance;
    }

    public List<Command> getCommands() {
        return commands;
    }

    public void loadCommands(String fileName) throws IOException {
        CommandList loaded = JsonUtils.deserializeCommands(fileName);
        this.commands = loaded.getCommands();
    }

    public void setCommands(List<Command> commands) {
        this.commands = commands;
    }
}
