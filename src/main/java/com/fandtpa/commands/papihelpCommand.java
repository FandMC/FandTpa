package com.fandtpa.commands;

import com.fandtpa.util.ChatColor; // 引入自定义的 ChatColor 枚举
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class papihelpCommand implements CommandExecutor {

    public papihelpCommand() {
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("papihelpCommand")) {
            sender.sendMessage(ChatColor.GREEN + "变量列表: " + ChatColor.AQUA + "%player%\n" +
                    "%online%\n" +
                    "%maxplayers%\n" +
                    "%world%\n" +
                    "%x%\n" +
                    "%y%\n" +
                    "%z%\n" +
                    "%tps%\n" +
                    "%tps1%\n" +
                    "%tps2%\n" +
                    "%tps3%\n" +
                    "%mspt%\n" +
                    "%health%\n" +
                    "%hunger%");
            return true;
        }
        return false;
    }
}
