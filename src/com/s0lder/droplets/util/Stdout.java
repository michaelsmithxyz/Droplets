package com.s0lder.droplets.util;

import com.s0lder.droplets.DropletsPlugin;
import com.s0lder.droplets.PluginConfiguration;
import java.util.logging.Level;

public class Stdout {
    
    public static void warn(String message) {
        DropletsPlugin.instance.getLogger().warning(message);
    }
    
    public static void error(String message) {
        DropletsPlugin.instance.getLogger().severe(message);
    }
    
    public static void info(String message) {
        DropletsPlugin.instance.getLogger().info(message);
    }
    
    public static void debug(String message) {
        if(PluginConfiguration.debugging.getBoolean()) {
            DropletsPlugin.instance.getLogger().log(Level.INFO, "[DEBUG] {0}", message);
        }
    }
    
    
}
