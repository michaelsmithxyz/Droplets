package com.s0lder.droplets;

import org.bukkit.Server;


public abstract class Droplet {

    public abstract void onEnable();
    
    public abstract void onDisable();
    
    protected DropletsPlugin getPlugin() {
        return DropletsPlugin.instance;
    }
    
    protected Server getServer() {
        return DropletsPlugin.instance.getServer();
    }

}
