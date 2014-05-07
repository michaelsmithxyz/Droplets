package com.s0lder.droplets;

import static com.s0lder.droplets.Messages._;
import com.s0lder.droplets.util.Loader;
import com.s0lder.droplets.util.Stdout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;


public class DropletsPlugin extends JavaPlugin {
    public static DropletsPlugin instance;
    
    private File config_file;
    private YamlConfiguration yaml_configuration;
    private Loader loader;
    private DropletManager dropletManager;
    
    @Override
    public void onEnable() {
        this.loader = new Loader(this);
        DropletsPlugin.instance = this;
        getDataFolder().mkdirs();
        setupConfiguration();
        Messages.initialize(PluginConfiguration.locale.getString());
        Stdout.info(_("localeSet", PluginConfiguration.locale.getString()));
        this.dropletManager = new DropletManager(this);
        this.dropletManager.enable();
        Stdout.info(_("enabled", getDescription().getFullName()));
    }

    @Override
    public void onDisable() {
        this.dropletManager.disable();
    }
    
    public DropletManager getDropletManager() {
        return this.dropletManager;
    }
    
    public ClassLoader getPluginClassLoader() {
        return this.getClassLoader();
    }
    
    public File getConfigFile() {
        return this.config_file;
    }
    
    public YamlConfiguration getYAMLConfiguration() {
        return this.yaml_configuration;
    }
    
    public Loader getDropletLoader() {
        return this.loader;
    }
    
    private void setupConfiguration() {
        try {
            this.config_file = new File(getDataFolder(), "config.yml");
            this.yaml_configuration = new YamlConfiguration();
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
                Stdout.info("Generated default configuration file");
                setupConfiguration();
            } catch(IOException ex){
                Stdout.error("Failed to generate default configuration file!");
            }
        } catch(IOException e) {
            Stdout.error("Could not read configuration file!");
        } catch(InvalidConfigurationException e) {
            Stdout.error("Malformed configuration file!");
        }
    }

}
