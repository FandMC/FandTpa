package com.fandtpa.commands.command;

import com.fandtpa.Main;
import com.fandtpa.manager.EcoManager;
import com.fandtpa.util.ChatColor;
import com.fandtpa.util.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MoneyCommand implements CommandExecutor {

    private final Main plugin;
    private final ConfigManager configManager;

    public MoneyCommand(Main plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("money_only_player")));
            return true;
        }

        EcoManager ecoManager = plugin.getEcoManager();
        String balance = ecoManager.getBalanceAsString(player.getUniqueId());

        String message = configManager.getMessage("money_balance");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message.replace("{balance}", balance)));
        return true;
    }
}
