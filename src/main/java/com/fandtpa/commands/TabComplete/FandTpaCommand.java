package com.fandtpa.commands.TabComplete;

import com.fandtpa.Main;
import com.fandtpa.util.ChatColor;
import com.fandtpa.manager.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FandTpaCommand implements CommandExecutor, TabCompleter {

    private final Main plugin;
    private final ConfigManager configManager;

    public FandTpaCommand(Main plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (args[0].toLowerCase().equals("reload")) {
            plugin.reloadConfig();
            configManager.reloadMessages();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("fandtpa_reload_success")));
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("unknown_subcommand")));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            if ("reload".startsWith(args[0].toLowerCase())) {
                completions.add("reload");
            }
        }
        return completions;
    }
}
