package com.fandtpa.commands;

import com.fandtpa.util.ChatColor;
import com.fandtpa.manager.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class SuicideCommand implements CommandExecutor {

    private final ConfigManager configManager;

    public SuicideCommand(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {

            forcePlayerDeath(player);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("suicide_success")));
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("suicide_only_player")));
        }
        return true;
    }

    private void forcePlayerDeath(Player player) {
        player.setHealth(0);
        if (!player.isDead()) {
            player.damage(10000);
        }
    }
}
