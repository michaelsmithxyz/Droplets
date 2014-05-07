package com.s0lder.droplets.command;

import com.s0lder.droplets.Droplet;
import com.s0lder.droplets.DropletsPlugin;
import static com.s0lder.droplets.Messages._;
import com.s0lder.droplets.annotations.Command;
import com.s0lder.droplets.util.Stdout;
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
    
    public void enable() {
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    public void registerCommands(Droplet droplet) {
        Stdout.debug(_("debugCommandsRegister", droplet.getName()));
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
                Stdout.debug(_("debugCommandFound", name));
                CommandHandler handler = new CommandHandler(name, cmd.aliases(),
                        cmd.description(), cmd.help());
                Class[] parameters = method.getParameterTypes();
                if(parameters.length != 1) {
                    Stdout.error(_("errorMismatchedArguments", name));
                    continue;
                }
                handler.setHandler(droplet, method);
                dropHandlers.add(handler);
            }
        }
    }
    
    public void unregisterCommands(Droplet droplet) {
        handlers.remove(droplet);
    }
    
    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
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
                        Stdout.error(_("errorFailedCommand", ex.getLocalizedMessage()));
                    }
                    event.setCancelled(true);
                }
            }
        }
    }
    
}
