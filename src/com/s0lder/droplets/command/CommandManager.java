package com.s0lder.droplets.command;

import com.s0lder.droplets.Droplet;
import com.s0lder.droplets.DropletsPlugin;
import static com.s0lder.droplets.Messages._;
import com.s0lder.droplets.annotations.Command;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandManager implements Listener {
    private Map<Droplet,List<CommandHandler>> handlers;
    private DropletsPlugin plugin;
    
    public CommandManager(DropletsPlugin plugin) {
        this.plugin = plugin;
        this.handlers = new HashMap<Droplet,List<CommandHandler>>();
    }
    
    public void registerCommands(Droplet droplet) {
        plugin.getLogger().info("Registering commands for: " + droplet.toString());
        Method[] methods = droplet.getClass().getDeclaredMethods();
        for(Method method : methods) {
            if(method.isAnnotationPresent(Command.class)) {
                List<CommandHandler> dropHandlers = handlers.get(droplet);
                if(dropHandlers == null) {
                    dropHandlers = new ArrayList<CommandHandler>();
                    handlers.put(droplet, dropHandlers);
                }
                Command cmd = method.getAnnotation(Command.class);
                String name = cmd.name();
                plugin.getLogger().info("Got command: " + name);
                CommandHandler handler = new CommandHandler(name, cmd.aliases(),
                        cmd.description(), cmd.help());
                Class[] parameters = method.getParameterTypes();
                if(parameters.length != 1) {
                    plugin.log.warning(_("mismatchedArguments", name));
                    continue;
                }
                handler.setHandler(droplet, method);
            }
        }
    }
    
    public void unregisterCommands(Droplet droplet) {
        handlers.remove(droplet);
    }
    
    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        plugin.getLogger().info("Preprocessing: " + event.getMessage());
        Player sender = event.getPlayer();
        String message = event.getMessage();
        CommandInfo info = CommandInfo.parseCommand(message, sender);
        for (Droplet drop : handlers.keySet()) {
            for (CommandHandler handler : handlers.get(drop)) {
                if (handler.matchCommand(info.getCommand())) {
                    Method method = handler.getHandler();
                    Droplet instance = handler.getInstance();
                    try {
                        method.invoke(instance, info);
                    } catch (Exception ex) {
                        DropletsPlugin.log.severe(_("failedCommand", ex.getLocalizedMessage()));
                    }
                    event.setCancelled(true);
                }
            }
        }
    }
    
}
