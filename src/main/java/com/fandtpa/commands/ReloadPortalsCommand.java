package com.fandtpa.commands;

import com.fandtpa.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadPortalsCommand implements CommandExecutor {

    private final Main plugin;

    public ReloadPortalsCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        plugin.loadPortals();
        sender.sendMessage("传送门配置已重新加载。");
        return true;
    }
}
