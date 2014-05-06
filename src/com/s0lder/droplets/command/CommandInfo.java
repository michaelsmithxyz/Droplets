package com.s0lder.droplets.command;

import com.s0lder.droplets.exceptions.MalformedCommandException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;

public class CommandInfo {
    private final String name;
    private final String[] args;
    private final Player sender;
    
    public CommandInfo(String name, String[] args, Player sender) {
        this.name = name;
        this.args = args;
        this.sender = sender;
    }
    
    public String getCommand() {
        return this.name;
    }
    
    public String[] getArguments() {
        return this.args;
    }
    
    public Player getSender() {
        return this.sender;
    }
    
    public static CommandInfo parseCommand(String command, Player sender) {
        String name;
        List<String> args = new ArrayList<String>();
        String[] parts = command.split(" ");
        if(parts.length < 1) {
            throw new MalformedCommandException("Failed to parse command: '" + command + "'");
        }
        name = parts[0].replaceFirst("/", "");
        if(parts.length > 1) {
            for(int i = 1; i < parts.length; i++) {
                args.add(parts[i]);
            }
        }
        return new CommandInfo(name, (String[]) args.toArray(), sender);
    }
    
}
