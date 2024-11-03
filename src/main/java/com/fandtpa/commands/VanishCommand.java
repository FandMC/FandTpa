package com.fandtpa.commands;

import com.fandtpa.Main;
import com.fandtpa.util.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class VanishCommand implements CommandExecutor {

    private final Main plugin;

    public VanishCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            plugin.toggleVanish(player);
            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "这个命令只能由玩家执行。");
            return false;
        }
    }
}
