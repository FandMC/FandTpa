package fand.fandtpa.commands;

import fand.fandtpa.Main;
import fand.fandtpa.util.ChatColor;
import fand.fandtpa.util.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetTitleCommand implements CommandExecutor {

    private final Main plugin;
    private final ConfigManager configManager;

    public SetTitleCommand(Main plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // 检查是否有权限
        if (!sender.hasPermission("titles.set")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("settitle_no_permission")));
            return true;
        }

        // 检查参数是否足够
        if (args.length < 2) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("settitle_usage")));
            return true;
        }

        // 查找玩家
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("settitle_player_not_found")));
            return true;
        }

        // 拼接称号
        StringBuilder titleBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            titleBuilder.append(args[i]).append(" ");
        }
        String title = titleBuilder.toString().trim();

        // 设置称号并保存到配置文件
        plugin.getTitlesConfig().set(target.getUniqueId().toString(), title);
        plugin.saveTitlesConfig();

        // 发送消息并更新玩家称号
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("settitle_success").replace("{title}", title).replace("{player}", target.getName())));
        target.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("settitle_received").replace("{title}", title)));
        target.displayName(net.kyori.adventure.text.Component.text(ChatColor.translateAlternateColorCodes('&', title + " " + target.getName())));

        return true;
    }
}
