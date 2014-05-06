package com.s0lder.droplets;

import com.s0lder.droplets.command.CommandManager;
import com.s0lder.droplets.exceptions.DropletLoadException;
import java.util.ArrayList;
import java.util.List;


public class DropletManager {
    private final DropletsPlugin plugin;
    private final CommandManager commandManager;
    private final List<Droplet> droplets;
    
    public DropletManager(DropletsPlugin plugin) {
        this.plugin = plugin;
        this.commandManager = new CommandManager(plugin);
        this.droplets = new ArrayList<Droplet>();
    }
    
    public CommandManager getCommandManager() {
        return this.commandManager;
    }
    
    public void addDroplet(Class<Droplet> droplet) {
        plugin.getLogger().info("Enabling droplet: " + droplet.getCanonicalName());
        try {
            Droplet dropletInst = droplet.newInstance();
            this.commandManager.registerCommands(dropletInst);
            droplets.add(dropletInst);
            dropletInst.onEnable();
        } catch (Exception ex) {
            throw new DropletLoadException(ex.getLocalizedMessage());
        }
    }
    
    public void removeDroplet(Droplet droplet) {
        if(droplets.contains(droplet)) {
            droplet.onDisable();
            commandManager.unregisterCommands(droplet);
            droplets.remove(droplet);
        }
    }
    
    public void removeAll() {
        for(int i = 0; i < droplets.size(); i++) {
            Droplet droplet = droplets.get(i);
            removeDroplet(droplet);
        }
    }
    
}
