package com.ajasielec.simulation.dto;

import com.ajasielec.simulation.utils.JsonUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandList {
    private static final CommandList instance = new CommandList();
    private final List<Command> commands = Collections.synchronizedList(new ArrayList<>());

    private CommandList() {}

    public static CommandList getInstance() {
        return instance;
    }

    public List<Command> getCommands() {
        return Collections.unmodifiableList(commands);
    }

    public void loadCommands(String fileName) throws IOException {
        CommandList loaded = JsonUtils.deserializeCommands(fileName);
        synchronized (commands) {
            commands.clear();
            commands.addAll(loaded.getCommands());
        }
    }

    public void setCommands(List<Command> newCommands) {
        synchronized (commands) {
            commands.clear();
            commands.addAll(newCommands);
        }
    }
}
