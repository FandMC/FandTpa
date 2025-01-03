package com.fandtpa.commands;

import com.fandtpa.util.ChatColor;
import com.fandtpa.manager.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FlyCommand implements CommandExecutor {

    private final ConfigManager configManager;

    public FlyCommand(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("command_only_player")));
            return true;
        }

        if (args.length == 0) {
            // 切换自己的飞行模式
            toggleFly(player, player);
            return true;
        } else if (args.length == 1) {
            // 切换其他玩家的飞行模式
            if (!player.hasPermission("fly.others")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("no_permission_others")));
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null || !target.isOnline()) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("player_not_found")));
                return true;
            }

            toggleFly(player, target);
            return true;
        }

        // 参数过多
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("command_usage_fly")));
        return true;
    }

    private void toggleFly(Player sender, Player target) {
        boolean canFly = target.getAllowFlight();
        target.setAllowFlight(!canFly);

        // 通知目标玩家
        target.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("fly_toggle")
                .replace("{status}", canFly ? configManager.getMessage("fly_disabled") : configManager.getMessage("fly_enabled"))));

        // 如果是给他人切换，通知执行者
        if (!sender.equals(target)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("fly_toggle_other")
                    .replace("{player}", target.getName())
                    .replace("{status}", canFly ? configManager.getMessage("fly_disabled") : configManager.getMessage("fly_enabled"))));
        }
    }
}
