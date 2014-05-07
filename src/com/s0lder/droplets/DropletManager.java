package com.s0lder.droplets;

import static com.s0lder.droplets.Messages._;
import com.s0lder.droplets.command.CommandManager;
import com.s0lder.droplets.exceptions.DropletLoadException;
import com.s0lder.droplets.exceptions.PackageLoadException;
import com.s0lder.droplets.util.Stdout;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.file.YamlConfiguration;


public class DropletManager {
    private final DropletsPlugin plugin;
    private final CommandManager commandManager;
    private final List<Droplet> droplets;
    
    public DropletManager(DropletsPlugin plugin) {
        this.plugin = plugin;
        this.commandManager = new CommandManager(plugin);
        this.droplets = new ArrayList<Droplet>();
    }
    
    public void enable() {
        YamlConfiguration config = this.plugin.getYAMLConfiguration();
        List<String> enabled = config.getStringList("enabled");
        if(enabled == null) {
            return;
        }
        loadDirectory(new File(plugin.getDataFolder(), PluginConfiguration.droplet_directory.getString()), enabled);
        this.commandManager.enable();
    }
    
    public void disable() {
        removeAll();
    }
    
    public CommandManager getCommandManager() {
        return this.commandManager;
    }
    
    public void addDroplet(Class<Droplet> droplet) {
        Stdout.debug(_("debugDropletEnable", droplet.getCanonicalName()));
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

    public void loadDirectory(File directory, List<String> droplets) {
        try {
            Stdout.debug(_("debugLoadPackage", directory.getPath()));
            this.plugin.getDropletLoader().addPackage(directory.getPath());
            for(String name : droplets) {
                try {
                    Class<Droplet> cls = this.plugin.getDropletLoader().getDroplet(name);
                    addDroplet(cls);
                } catch(DropletLoadException e) {
                    Stdout.error(_("errorDropletLoad", name));
                }
            }
        } catch(PackageLoadException ex) {
            Stdout.error(_("errorPackageLoad", directory.getPath()));
        }
    }
    
}
