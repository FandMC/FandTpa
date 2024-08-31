package fand.fandtpa.commands;

import fand.fandtpa.Main;
import fand.fandtpa.util.ChatColor;
import fand.fandtpa.util.ConfigManager;
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
        if (args.length == 0) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("fandtpa_usage")));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                if (!sender.hasPermission("fandtpa.reload")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("no_permission")));
                    return true;
                }
                plugin.reloadConfig();
                configManager.reloadMessages();
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("fandtpa_reload_success")));
                break;

            case "help":
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("fandtpa_help")));
                break;

            default:
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("unknown_subcommand").replace("{command}", args[0])));
                break;
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
            if ("help".startsWith(args[0].toLowerCase())) {
                completions.add("help");
            }
        }
        return completions;
    }
}
