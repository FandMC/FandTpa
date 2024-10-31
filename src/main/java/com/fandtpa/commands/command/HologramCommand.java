package com.fandtpa.commands.command;

import com.fandtpa.Main;
import com.fandtpa.util.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class HologramCommand implements CommandExecutor {

    private final Main plugin;

    public HologramCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            plugin.reloadHolograms();
            sender.sendMessage(ChatColor.GREEN + "所有悬浮字已重载。");
            return true;
        }
        sender.sendMessage(ChatColor.RED + "用法: /hd reload");
        return false;
    }
}
