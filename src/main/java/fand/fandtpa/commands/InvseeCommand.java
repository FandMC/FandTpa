package fand.fandtpa.commands;

import fand.fandtpa.util.ChatColor;
import fand.fandtpa.util.ConfigManager;
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
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("invsee_only_player")));
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("invsee_usage")));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target != null && target.isOnline()) {
            openCustomInventoryView(player, target);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("invsee_viewing").replace("{target}", target.getName())));
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("invsee_player_not_found")));
        }

        return true;
    }

    private void openCustomInventoryView(Player viewer, Player target) {
        Inventory customInventory = Bukkit.createInventory(null, 54, target.getName() + configManager.getMessage("invsee_inventory_suffix"));

        ItemStack[] inventoryContents = target.getInventory().getContents();
        System.arraycopy(inventoryContents, 0, customInventory.getContents(), 0, Math.min(inventoryContents.length, 36));

        customInventory.setItem(36, target.getInventory().getItemInOffHand());

        ItemStack[] armorContents = target.getInventory().getArmorContents();
        System.arraycopy(armorContents, 0, customInventory.getContents(), 45, armorContents.length);

        viewer.openInventory(customInventory);
    }
}
