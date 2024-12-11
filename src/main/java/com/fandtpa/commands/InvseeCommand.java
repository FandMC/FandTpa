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
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "只有玩家可以使用这个命令！");
            return true;
        }

        Player player = (Player) sender;

        // 权限检查
        if (!player.hasPermission("fandtpa.invsee.use")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("no_permission")));
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

        // 创建目标玩家的背包界面
        openInventory(player, target);

        return true;
    }

    private void openInventory(Player viewer, Player target) {
        // 创建一个容量为 45 的自定义背包界面
        Inventory inventory = Bukkit.createInventory(viewer, 54, "查看 " + target.getName() + " 的背包");

        // 将目标玩家的背包内容填充到自定义的背包界面
        // 背包内容放在前 27 格（上面三行）
        inventory.setContents(target.getInventory().getContents());

        // 将目标玩家的物品栏内容放在第 28-36 格
        inventory.setItem(27, target.getInventory().getItemInMainHand());
        inventory.setItem(28, target.getInventory().getItemInOffHand());

        // 将目标玩家的装备放在 36-44 格（盔甲和副手）
        ItemStack[] armorContents = target.getInventory().getArmorContents();
        for (int i = 0; i < armorContents.length; i++) {
            inventory.setItem(36 + i, armorContents[i]);
        }

        // 打开背包界面
        viewer.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        // 判断是否是查看其他玩家背包的界面
        if (event.getView().getTitle().startsWith("查看 ")) {
            // 只有目标背包的查看者才可操作
            if (!player.hasPermission("fandtpa.invsee.use")) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("no_permission")));
                return;
            }

            Player target = Bukkit.getPlayer(event.getView().getTitle().substring(3).split(" ")[0]);

            if (target == null || !target.isOnline()) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("invsee_player_not_found")));
                return;
            }

            // 获取点击的物品
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem != null && clickedItem.getType() != Material.AIR) {
                // 允许随意拿取或放入物品
                if (event.getAction().name().contains("DROP")) {
                    // 放物品到目标背包
                    target.getInventory().addItem(clickedItem);
                } else if (event.getAction().name().contains("PICKUP")) {
                    // 拿走物品
                    target.getInventory().removeItem(clickedItem);
                }
            }

            event.setCancelled(true);  // 禁止默认的背包操作，防止直接放置或拿取
        }
    }
}
