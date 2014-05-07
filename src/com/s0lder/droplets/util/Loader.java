package com.s0lder.droplets.util;

import com.s0lder.droplets.Droplet;
import com.s0lder.droplets.DropletsPlugin;
import com.s0lder.droplets.exceptions.DropletLoadException;
import com.s0lder.droplets.exceptions.PackageLoadException;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;


public class Loader {
    private DropletsPlugin plugin;
    
    public Loader(DropletsPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void addPackage(String path) {
        try {
            File pathFile = new File(path);
            URL url = pathFile.toURI().toURL();
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
            method.setAccessible(true);
            method.invoke(plugin.getPluginClassLoader(), new Object[]{url});
        } catch (Exception ex) {
            throw new PackageLoadException(ex.getLocalizedMessage());
        }    
    }
    
    public Class<Droplet> getDroplet(String name) {
        try {
            Class<Droplet> module_class = (Class<Droplet>) Class.forName(name);
            return module_class;
        } catch (ClassNotFoundException ex) {
            throw new DropletLoadException(ex.getLocalizedMessage());
        }
    }
    
}
