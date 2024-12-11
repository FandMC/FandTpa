package com.fandtpa.commands;

import com.fandtpa.Main;
import com.fandtpa.listeners.VeinMineListener;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class VeinMineCommand implements CommandExecutor {

    private final Main plugin;
    private boolean veinMiningEnabled = false; // 默认关闭连锁挖矿

    public VeinMineCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player && sender.hasPermission("veinmine.toggle")) {
            toggleVeinMining();
            String status = isVeinMiningEnabled() ? "开启" : "关闭";
            sender.sendMessage("连锁挖矿已" + status);
            return true;
        }
        sender.sendMessage("你没有权限使用此命令！");
        return false;
    }

    public boolean isVeinMiningEnabled() {
        return veinMiningEnabled;
    }

    public void toggleVeinMining() {
        veinMiningEnabled = !veinMiningEnabled;
        if (veinMiningEnabled) {
            Bukkit.getPluginManager().registerEvents(new VeinMineListener(plugin), plugin);
        } else {
            VeinMineListener.unregister(plugin);
        }
    }
}
