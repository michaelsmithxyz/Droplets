package com.s0lder.droplets;

import static com.s0lder.droplets.Messages._;
import com.s0lder.droplets.command.CommandManager;
import com.s0lder.droplets.util.Loader;
import com.s0lder.droplets.util.Stdout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;


public class DropletsPlugin extends JavaPlugin {
    public static Logger log;
    public static File config_file;
    public static YamlConfiguration yaml_configuration;
    public static Loader loader;
    public static DropletsPlugin instance;
    
    private DropletManager dropletManager;
    
    @Override
    public void onEnable() {
        log = getLogger();
        loader = new Loader(this);
        instance = this;
        getDataFolder().mkdirs();
        setupConfiguration();
        Messages.initialize(PluginConfiguration.locale.getString());
        Stdout.info(_("localeSet", PluginConfiguration.locale.getString()));
        this.dropletManager = new DropletManager(this);
        Class<Droplet> test = loader.getDroplet(new File(getDataFolder(), "droplets").getPath(), "TestDroplet");
        this.dropletManager.addDroplet(test);
        Stdout.info(_("enabled", getDescription().getFullName()));
    }

    @Override
    public void onDisable() {
        
    }
    
    public DropletManager getDropletManager() {
        return this.dropletManager;
    }
    
    public ClassLoader getPluginClassLoader() {
        return this.getClassLoader();
    }
    
    private void setupConfiguration() {
        try {
            config_file = new File(getDataFolder(), "config.yml");
            yaml_configuration = new YamlConfiguration();
            yaml_configuration.load(config_file);
            PluginConfiguration.load(yaml_configuration);
        } catch(FileNotFoundException e) {
            try {
                InputStream configStream = getClass().getResourceAsStream("/res/config.yml");
                config_file.createNewFile();
                OutputStream out = new FileOutputStream(config_file);
                int next;
                while((next = configStream.read()) != -1) {
                    out.write(next);
                }
                log.info("Generated default configuration file");
                setupConfiguration();
            } catch(IOException ex){
                log.severe("Failed to generate default configuration file!");
            }
        } catch(IOException e) {
            log.severe("Could not read configuration file!");
        } catch(InvalidConfigurationException e) {
            log.severe("Malformed configuration file!");
        }
    }

}
