package com.fandtpa.register;

import com.fandtpa.Main;
import com.fandtpa.commands.BackCommand;
import com.fandtpa.manager.OtpManager;
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
        Bukkit.getPluginManager().registerEvents(new PlayerChatListener(plugin), plugin);
        BackCommand backCommand = new BackCommand(plugin.getLogger(), plugin.getConfigManager());
        Bukkit.getPluginManager().registerEvents(backCommand, plugin);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(otpManager), plugin);
        Bukkit.getPluginManager().registerEvents(new PortalListener(plugin), plugin);
    }
}
