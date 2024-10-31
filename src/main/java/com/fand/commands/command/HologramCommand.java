package com.fand.commands.command;

import com.fand.Main;
import com.fand.util.ChatColor;
import com.fand.manager.HologramsManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class HologramCommand implements CommandExecutor {
    private HologramsManager hologramsManager;
    private final Main plugin;

    public HologramCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        hologramsManager = new HologramsManager();
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            hologramsManager.reloadHolograms();
            sender.sendMessage(ChatColor.GREEN + "所有悬浮字已重载。");
            return true;
        }
        sender.sendMessage(ChatColor.RED + "用法: /hd reload");
        return false;
    }
}
