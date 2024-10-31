package com.fandtpa.commands.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FServerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("用法: /fserver <子服>");
            return true;
        }

        String subServer = args[0];
        if (Bukkit.getServer().getWorld(subServer) != null) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                sender.sendMessage("子服 " + subServer + " 当前有玩家 " + player.getName());
            }
        } else {
            sender.sendMessage("子服 " + subServer + " 不存在或不可用。");
        }
        return true;
    }
}
