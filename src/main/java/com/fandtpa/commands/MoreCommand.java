package com.fandtpa.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MoreCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            ItemStack itemInHand = player.getInventory().getItemInMainHand();
            if (itemInHand == null || itemInHand.getType().isAir()) {
                player.sendMessage("§c你手上没有任何物品！");
            } else {
                itemInHand.setAmount(64);
                player.getInventory().setItemInMainHand(itemInHand);
                player.sendMessage("§a手上的物品数量已强制设置为 64！");
            }
            return true;
        } else {
            sender.sendMessage("§c只有玩家才能使用此命令！");
            return true;
        }
    }
}
