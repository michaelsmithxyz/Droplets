package com.s0lder.droplets.command;

import com.s0lder.droplets.Droplet;
import com.s0lder.droplets.util.Stdout;
import java.lang.reflect.Method;

public class CommandHandler {
    private final String command;
    private final String[] aliases;
    private final String[] description;
    private final String[] help;
    private Method handler;
    private Droplet instance;
    
    public CommandHandler(String command, String[] aliases, String[] description, String[] help) {
        this.command = command;
        this.aliases = aliases;
        this.description = description;
        this.help = help;
        this.handler = null;
        this.instance = null;
    }
    
    public void setHandler(Droplet instance, Method handler) {
        this.instance = instance;
        this.handler = handler;
    }
    
    public Method getHandler() {
        return this.handler;
    }
    
    public Droplet getInstance() {
        return this.instance;
    }
    
    public String getCommand() {
        return this.command;
    }
    
    public String[] getAliases() {
        return this.aliases;
    }
    
    public String[] getDescription() {
        return this.description;
    }
    
    public String[] getHelp() {
        return this.help;
    }
    
    public boolean matchCommand(String command) {
        if(command.equalsIgnoreCase(this.command)) {
            return true;
        } else {
            for(String alias : this.aliases) {
                if(alias.equalsIgnoreCase(command)) {
                    return true;
                }
            }
            return false;
        }
    }
    
}
