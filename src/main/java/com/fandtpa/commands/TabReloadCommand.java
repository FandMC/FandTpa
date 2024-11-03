package com.fandtpa.commands;

import com.fandtpa.Main;
import com.fandtpa.util.ChatColor;
import com.fandtpa.manager.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class TabReloadCommand implements CommandExecutor {

    private final Main plugin;
    private final ConfigManager configManager;

    public TabReloadCommand(Main plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            plugin.loadTabConfig();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("tab_reload_success")));
            return true;
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("tab_reload_usage")));
        return false;
    }
}
