package com.s0lder.droplets;

import org.bukkit.configuration.file.YamlConfiguration;


public enum PluginConfiguration {
    
    debugging("debugging", false),
    locale("locale", "en_US"),
    droplet_directory("droplet_directory", "droplets");
    
    private String path;
    private Object value;
    
    private PluginConfiguration(String path, Object value) {
        this.path = path;
        this.value = value;
    }
    
    public String getPath() {
        return path;
    }
    
    public Boolean getBoolean() {
        return (Boolean) value;
    }
    
    public Integer getInteger() {
        return (Integer) value;
    }
    
    public String getString() {
        return (String) value;
    }
    
    public void setValue(Object obj) {
        this.value = obj;
    }
    
    public static void load(YamlConfiguration yaml) {
        for(PluginConfiguration c : values()) {
            if(!c.getPath().isEmpty()) {
                if(yaml.get(c.getPath()) != null) {
                    c.setValue(yaml.get(c.getPath()));
                }
            }
        }
    }

}
