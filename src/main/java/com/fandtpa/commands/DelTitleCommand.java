package com.fandtpa.commands;

import com.fandtpa.Main;
import com.fandtpa.util.ChatColor;
import com.fandtpa.manager.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DelTitleCommand implements CommandExecutor {

    private final Main plugin;
    private final ConfigManager configManager;

    public DelTitleCommand(Main plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (args.length < 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("deltitle_usage")));
            return true;
        }

        String targetName = args[0];
        Player target = Bukkit.getPlayer(targetName);

        if (target == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("deltitle_player_not_found")));
            return true;
        }

        String uuid = target.getUniqueId().toString();
        plugin.getTitlesConfig().set(uuid, null);
        plugin.saveTitlesConfig();

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("deltitle_success").replace("{player}", target.getName())));
        target.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("deltitle_received").replace("{player}", target.getName())));

        target.displayName(net.kyori.adventure.text.Component.text(target.getName()));

        return true;
    }
}
