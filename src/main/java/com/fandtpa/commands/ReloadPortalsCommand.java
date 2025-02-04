package com.fandtpa.commands;

import com.fandtpa.Main;
import com.fandtpa.manager.ConfigManager;
import com.fandtpa.util.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadPortalsCommand implements CommandExecutor {

    private final Main plugin;
    private final ConfigManager configManager;
    public ReloadPortalsCommand(Main plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        plugin.loadPortals();
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("portals_reload")));
        return true;
    }
}
