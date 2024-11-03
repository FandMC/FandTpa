package com.fandtpa.commands;

import com.fandtpa.util.ChatColor;
import com.fandtpa.manager.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpeedCommand implements CommandExecutor {

    private final ConfigManager configManager;

    public SpeedCommand(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("speed_only_player")));
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("speed_usage")));
            return true;
        }

        String mode = args[0].toLowerCase();
        double speed;

        try {
            speed = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("speed_invalid_number")));
            return true;
        }

        if (speed < 0.1 || speed > 1.0) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("speed_invalid_range")));
            return true;
        }

        switch (mode) {
            case "walk":
                player.setWalkSpeed((float) speed);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("speed_walk_set")
                        .replace("{speed}", String.valueOf(speed))));
                break;
            case "fly":
                if (!player.getAllowFlight()) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("speed_fly_not_allowed")));
                    return true;
                }
                player.setFlySpeed((float) speed);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("speed_fly_set")
                        .replace("{speed}", String.valueOf(speed))));
                break;
            default:
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("speed_usage")));
                break;
        }

        return true;
    }
}
