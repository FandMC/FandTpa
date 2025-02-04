package com.fandtpa.commands;

import com.fandtpa.Main;
import com.fandtpa.manager.ConfigManager;
import com.fandtpa.util.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

public class VanishCommand implements CommandExecutor {

    private final Main plugin;
    private final ConfigManager configManager;

    public VanishCommand(Main plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            toggleVanish(player);
            return true;
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("player_only")));
            return false;
        }
    }
    public void toggleVanish(Player player) {
        if (player.hasMetadata("vanished")) {
            player.removeMetadata("vanished", plugin);
            Bukkit.getOnlinePlayers().forEach(p -> p.showPlayer(plugin, player));
            player.sendMessage(ChatColor.GREEN + "你现在已取消隐身！");
        } else {
            player.setMetadata("vanished", new FixedMetadataValue(plugin, true));
            Bukkit.getOnlinePlayers().forEach(p -> p.hidePlayer(plugin, player));
            player.sendMessage(ChatColor.GREEN + "你现在已隐身！");
        }
    }

    public Main getPlugin() {
        return plugin;
    }
}
