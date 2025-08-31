package com.coreflux.mqtt.commands;

import com.coreflux.mqtt.MinecraftMQTTStreamer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.List;

public class MQTTCommand implements CommandExecutor, TabCompleter {
    
    private final MinecraftMQTTStreamer plugin;
    
    public MQTTCommand(MinecraftMQTTStreamer plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§6[MQTT Streamer] §fUsage: /mqtt <reload|status|test>");
            return true;
        }
        
        String subCommand = args[0].toLowerCase();
        
        switch (subCommand) {
            case "reload":
                if (sender.hasPermission("mqtt.admin")) {
                    plugin.reload();
                    sender.sendMessage("§6[MQTT Streamer] §aConfiguration reloaded.");
                } else {
                    sender.sendMessage("§cYou don't have permission to do that.");
                }
                break;
            case "status":
                if (sender.hasPermission("mqtt.admin")) {
                    boolean isConnected = plugin.getMqttManager().isConnected();
                    sender.sendMessage("§6[MQTT Streamer] §fStatus: " + 
                                       (isConnected ? "§aConnected" : "§cDisconnected"));
                } else {
                    sender.sendMessage("§cYou don't have permission to do that.");
                }
                break;
            case "test":
                if (sender.hasPermission("mqtt.admin")) {
                    sender.sendMessage("§6[MQTT Streamer] §eTest command not implemented yet.");
                } else {
                    sender.sendMessage("§cYou don't have permission to do that.");
                }
                break;
            default:
                sender.sendMessage("§6[MQTT Streamer] §cUnknown command. Usage: /mqtt <reload|status|test>");
                break;
        }
        
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("reload", "status", "test");
        }
        return null;
    }
}
