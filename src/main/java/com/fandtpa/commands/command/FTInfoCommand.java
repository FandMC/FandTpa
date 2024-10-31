package com.fandtpa.commands.command;

import com.fandtpa.util.ChatColor; // 引入自定义的 ChatColor 枚举
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class FTInfoCommand implements CommandExecutor {

    private final JavaPlugin plugin;

    // 构造函数，传递插件实例
    public FTInfoCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    // 当执行 /ftinfo 时，这个方法会被调用
    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("ftinfo")) {
            // 获取插件的版本信息
            String version = plugin.getDescription().getVersion();

            // 检测 PlaceholderAPI 插件是否存在
            Plugin placeholderAPI = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
            String placeholderMessage;

            if (placeholderAPI != null) {
                placeholderMessage = ChatColor.GREEN + "已联动PAPI变量插件为计分板提供变量";
            } else {
                placeholderMessage = ChatColor.RED + "未联动任何插件";
            }

            // 向玩家发送信息
            sender.sendMessage(ChatColor.GREEN + "FandTPA 插件版本: " + ChatColor.YELLOW + version);
            sender.sendMessage(ChatColor.GREEN + "开发者: " + ChatColor.AQUA + "Fand");
            sender.sendMessage(placeholderMessage);
            sender.sendMessage(ChatColor.GREEN + "感谢使用本插件！");
            return true;
        }
        return false;
    }
}
