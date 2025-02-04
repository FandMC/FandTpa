package com.fandtpa.commands;

import com.fandtpa.Main;
import com.fandtpa.manager.ConfigManager;
import com.fandtpa.manager.Holograms;
import com.fandtpa.util.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class HologramCommand implements CommandExecutor {

    Holograms holograms;
    private final ConfigManager configManager;
    public HologramCommand(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            holograms.reloadHolograms();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("Done")));
            return true;
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("hd_usage")));
        return false;
    }
}
