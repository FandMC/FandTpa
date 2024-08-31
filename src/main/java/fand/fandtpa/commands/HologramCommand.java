package fand.fandtpa.commands;

import fand.fandtpa.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class HologramCommand implements CommandExecutor {

    private final Main plugin;

    public HologramCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            plugin.reloadHolograms();
            sender.sendMessage(ChatColor.GREEN + "所有悬浮字已重载。");
            return true;
        }
        sender.sendMessage(ChatColor.RED + "用法: /hd reload");
        return false;
    }
}
