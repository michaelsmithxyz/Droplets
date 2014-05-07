package com.s0lder.droplets;

import org.bukkit.Server;


public abstract class Droplet {

    public void onEnable() {};
    
    public void onDisable() {};
    
    public abstract String getName();
    
    protected DropletsPlugin getPlugin() {
        return DropletsPlugin.instance;
    }
    
    protected Server getServer() {
        return DropletsPlugin.instance.getServer();
    }
    
    protected DropletManager getDropletManager() {
        return DropletsPlugin.instance.getDropletManager();
    }

}
