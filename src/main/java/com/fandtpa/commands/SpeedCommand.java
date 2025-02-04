package com.fandtpa.commands;

import com.fandtpa.util.ChatColor;
import com.fandtpa.manager.ConfigManager;
import org.bukkit.Bukkit;
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
        if (args.length < 2 || args.length > 3) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("speed_usage")));
            return true;
        }

        String mode = args[0].toLowerCase();
        if (!mode.equals("walk") && !mode.equals("fly")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("speed_usage")));
            return true;
        }

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

        Player target;
        if (args.length == 3) {
            if (!sender.hasPermission("speed.others")) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("speed_no_permission_others")));
                return true;
            }

            target = Bukkit.getPlayer(args[2]);
            if (target == null || !target.isOnline()) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("speed_player_not_found")));
                return true;
            }
        } else {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("speed_only_player")));
                return true;
            }
            target = player;
        }

        switch (mode) {
            case "walk":
                target.setWalkSpeed((float) speed);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("speed_walk_set")
                        .replace("{speed}", String.valueOf(speed))
                        .replace("{player}", target.getName())));
                if (!sender.equals(target)) {
                    target.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("speed_walk_set_other")
                            .replace("{speed}", String.valueOf(speed))));
                }
                break;

            case "fly":
                if (!target.getAllowFlight()) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("speed_fly_not_allowed")));
                    return true;
                }
                target.setFlySpeed((float) speed);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("speed_fly_set")
                        .replace("{speed}", String.valueOf(speed))
                        .replace("{player}", target.getName())));
                if (!sender.equals(target)) {
                    target.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("speed_fly_set_other")
                            .replace("{speed}", String.valueOf(speed))));
                }
                break;
        }

        return true;
    }
}
