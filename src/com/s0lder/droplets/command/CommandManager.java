package com.s0lder.droplets.command;

import com.s0lder.droplets.DropletsPlugin;
import static com.s0lder.droplets.Messages._;
import com.s0lder.droplets.annotations.Command;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandManager implements Listener {
    private List<CommandHandler> handlers;
    private DropletsPlugin plugin;
    
    public CommandManager(DropletsPlugin plugin) {
        this.plugin = plugin;
        this.handlers = new ArrayList<CommandHandler>();
    }
    
    public void registerCommands(Object droplet) {
        Method[] methods = droplet.getClass().getDeclaredMethods();
        for(Method method : methods) {
            if(method.isAnnotationPresent(Command.class)) {
                Command cmd = method.getAnnotation(Command.class);
                String name = cmd.name();
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
    
    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player sender = event.getPlayer();
        String message = event.getMessage();
        CommandInfo info = CommandInfo.parseCommand(message, sender);
        for(CommandHandler handler : this.handlers) {
            if(handler.matchCommand(info.getCommand())) {
                Method method = handler.getHandler();
                Object instance = handler.getInstance();
                try {
                    method.invoke(instance, info);
                } catch (Exception ex) {
                    DropletsPlugin.log.severe(_("failedCommand", ex.getLocalizedMessage()));
                }
            }
        }
    }
    
}
