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
    private boolean veinMiningEnabled = false;

    public VeinMineCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        toggleVeinMining();
        String status = isVeinMiningEnabled() ? "开启" : "关闭";
        sender.sendMessage("连锁挖矿已" + status);
        return true;
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
