package com.fandtpa.commands;

import com.fandtpa.util.ChatColor;
import com.fandtpa.manager.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class InvseeCommand implements CommandExecutor, Listener {

    private final ConfigManager configManager;

    public InvseeCommand(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "只有玩家可以使用这个命令！");
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

        openInventory(player, target);

        return true;
    }

    private void openInventory(Player viewer, Player target) {
        Inventory inventory = Bukkit.createInventory(viewer, 54, "查看 " + target.getName() + " 的背包");

        inventory.setContents(target.getInventory().getContents());

        inventory.setItem(27, target.getInventory().getItemInMainHand());
        inventory.setItem(28, target.getInventory().getItemInOffHand());

        ItemStack[] armorContents = target.getInventory().getArmorContents();
        for (int i = 0; i < armorContents.length; i++) {
            inventory.setItem(36 + i, armorContents[i]);
        }

        viewer.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getView().getTitle().startsWith("查看 ")) {

            Player target = Bukkit.getPlayer(event.getView().getTitle().substring(3).split(" ")[0]);

            if (target == null || !target.isOnline()) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("invsee_player_not_found")));
                return;
            }

            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem != null && clickedItem.getType() != Material.AIR) {

                if (event.getAction().name().contains("DROP")) {

                    target.getInventory().addItem(clickedItem);
                } else if (event.getAction().name().contains("PICKUP")) {

                    target.getInventory().removeItem(clickedItem);
                }
            }

            // event.setCancelled(true);
        }
    }
}
