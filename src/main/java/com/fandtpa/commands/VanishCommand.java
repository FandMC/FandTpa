package com.fandtpa.commands;

import com.fandtpa.Main;
import com.fandtpa.util.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

public class VanishCommand implements CommandExecutor {

    private final Main plugin;

    public VanishCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            toggleVanish(player);
            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "这个命令只能由玩家执行。");
            return false;
        }
    }
    public void toggleVanish(Player player) {
        if (player.hasMetadata("vanished")) {
            // 解除隐身状态
            player.removeMetadata("vanished", plugin);
            Bukkit.getOnlinePlayers().forEach(p -> p.showPlayer(plugin, player));
            player.sendMessage(ChatColor.GREEN + "你现在已取消隐身！");
        } else {
            // 设置隐身状态
            player.setMetadata("vanished", new FixedMetadataValue(plugin, true));
            Bukkit.getOnlinePlayers().forEach(p -> p.hidePlayer(plugin, player));
            player.sendMessage(ChatColor.GREEN + "你现在已隐身！");
        }
    }

    public Main getPlugin() {
        return plugin;
    }
}
