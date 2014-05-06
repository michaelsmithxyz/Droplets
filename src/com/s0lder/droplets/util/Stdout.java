package com.s0lder.droplets.util;

import com.s0lder.droplets.DropletsPlugin;
import com.s0lder.droplets.PluginConfiguration;

public class Stdout {
    
    public static void warn(String message) {
        DropletsPlugin.log.warning(message);
    }
    
    public static void error(String message) {
        DropletsPlugin.log.severe(message);
    }
    
    public static void info(String message) {
        DropletsPlugin.log.info(message);
    }
    
    public static void debug(String message) {
        if(PluginConfiguration.debugging.getBoolean()) {
            DropletsPlugin.log.info("[DEBUG] " + message);
        }
    }
    
    
}
