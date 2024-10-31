package com.fand.commands.command;

import com.fand.util.ChatColor;
import com.fand.util.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class HatCommand implements CommandExecutor {

    private final ConfigManager configManager;

    public HatCommand(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("hat_only_player")));
            return true;
        }

        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if (itemInHand.getType().isAir()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("hat_no_item_in_hand")));
            return true;
        }

        ItemStack itemOnHead = player.getInventory().getHelmet();
        player.getInventory().setHelmet(itemInHand);
        player.getInventory().setItemInMainHand(itemOnHead);

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("hat_success")));

        return true;
    }
}
