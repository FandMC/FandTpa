package com.fandtpa.commands.command;

import com.fandtpa.Main;
import com.fandtpa.util.ChatColor;
import com.fandtpa.util.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetTitleCommand implements CommandExecutor {

    private final Main plugin;
    private final ConfigManager configManager;

    public SetTitleCommand(Main plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!sender.hasPermission("titles.set")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("settitle_no_permission")));
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("settitle_usage")));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("settitle_player_not_found")));
            return true;
        }

        StringBuilder titleBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            titleBuilder.append(args[i]).append(" ");
        }
        String title = titleBuilder.toString().trim();

        plugin.getTitlesConfig().set(target.getUniqueId().toString(), title);
        plugin.saveTitlesConfig();

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("settitle_success").replace("{title}", title).replace("{player}", target.getName())));
        target.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("settitle_received").replace("{title}", title)));
        target.displayName(net.kyori.adventure.text.Component.text(ChatColor.translateAlternateColorCodes('&', title + " " + target.getName())));

        return true;
    }
}
