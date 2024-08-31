package fand.fandtpa.commands;

import fand.fandtpa.util.ChatColor;
import fand.fandtpa.util.ConfigManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class InvseeCommand implements CommandExecutor {

    private final ConfigManager configManager;

    public InvseeCommand(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("invsee_only_player")));
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("invsee_usage")));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null || !target.isOnline()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("invsee_player_not_found")));
            return true;
        }

        if (target.equals(player)) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("invsee_cannot_view_own_inventory")));
            return true;
        }

        openCustomInventoryView(player, target);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("invsee_viewing").replace("{target}", target.getName())));

        return true;
    }

    private void openCustomInventoryView(Player viewer, Player target) {
        // 使用 Component 替代 String 创建标题
        Inventory customInventory = Bukkit.createInventory(null, 54, Component.text(target.getName() + configManager.getMessage("invsee_inventory_suffix")));

        ItemStack[] inventoryContents = target.getInventory().getContents();
        for (int i = 0; i < 36; i++) {
            if (inventoryContents[i] != null) {
                customInventory.setItem(i, inventoryContents[i].clone());
            }
        }

        customInventory.setItem(36, target.getInventory().getItemInOffHand().clone());

        ItemStack[] armorContents = target.getInventory().getArmorContents();
        for (int i = 0; i < armorContents.length; i++) {
            if (armorContents[i] != null) {
                customInventory.setItem(45 + i, armorContents[i].clone());
            }
        }

        viewer.openInventory(customInventory);
    }
}
