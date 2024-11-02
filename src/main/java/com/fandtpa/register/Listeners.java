package com.fandtpa.register;

import com.fandtpa.Main;
import com.fandtpa.commands.command.BackCommand;
import com.fandtpa.manager.listeners.OtpManager;
import com.fandtpa.manager.listeners.PlayerChatListener;
import com.fandtpa.manager.listeners.PlayerQuitListener;
import com.fandtpa.manager.listeners.PortalListener;
import org.bukkit.Bukkit;

public class Listeners {
    private final Main plugin;
    private final OtpManager otpManager;

    public Listeners(Main plugin, OtpManager otpManager) {
        this.plugin = plugin;
        this.otpManager = otpManager;
        registerListeners();
    }

    private void registerListeners() {
        // 注册聊天监听器
        Bukkit.getPluginManager().registerEvents(new PlayerChatListener(plugin), plugin);

        // 注册后退命令监听器
        BackCommand backCommand = new BackCommand(plugin.getLogger(), plugin.getConfigManager());
        Bukkit.getPluginManager().registerEvents(backCommand, plugin);

        // 注册玩家退出监听器
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(otpManager), plugin);

        // 注册传送门监听器
        Bukkit.getPluginManager().registerEvents(new PortalListener(plugin), plugin);

        // 可以在这里添加更多监听器
    }
}
