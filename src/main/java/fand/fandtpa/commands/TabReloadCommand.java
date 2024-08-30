package fand.fandtpa.commands;

import fand.fandtpa.Main;
import fand.fandtpa.util.ChatColor;
import fand.fandtpa.util.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TabReloadCommand implements CommandExecutor {

    private final Main plugin;
    private final ConfigManager configManager;

    public TabReloadCommand(Main plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            plugin.loadTabConfig(); // 重新加载配置文件
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("tab_reload_success")));
            return true;
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("tab_reload_usage")));
        return false;
    }
}