package com.fandtpa;

import com.fandtpa.commands.*;
import com.fandtpa.commands.TabComplete.*;
import com.fandtpa.listeners.*;
import com.fandtpa.manager.*;
import org.bukkit.Bukkit;

import java.util.Objects;

public class Registers {
    private final Main plugin;
    private final ConfigManager configManager;
    private final OtpManager otpManager;

    public Registers(Main plugin, ConfigManager configManager, OtpManager otpManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.otpManager = otpManager;
        registerCommands();
        registerTabCompleters();
        registerListeners();
    }

    private void registerCommands() {
        Objects.requireNonNull(plugin.getCommand("tpa")).setExecutor(new TpaCommand(configManager));
        Objects.requireNonNull(plugin.getCommand("tpahere")).setExecutor(new TpahereCommand(configManager));
        Objects.requireNonNull(plugin.getCommand("tpaccept")).setExecutor(new TpacceptCommand(plugin, configManager));
        Objects.requireNonNull(plugin.getCommand("tpdeny")).setExecutor(new TpdenyCommand(plugin, configManager));
        Objects.requireNonNull(plugin.getCommand("home")).setExecutor(new HomeCommand(plugin, configManager));
        Objects.requireNonNull(plugin.getCommand("back")).setExecutor(new BackCommand(plugin.getLogger(), configManager));
        Objects.requireNonNull(plugin.getCommand("settitle")).setExecutor(new SetTitleCommand(plugin, configManager));
        Objects.requireNonNull(plugin.getCommand("deltitle")).setExecutor(new DelTitleCommand(plugin, configManager));
        Objects.requireNonNull(plugin.getCommand("suicide")).setExecutor(new SuicideCommand(configManager));
        Objects.requireNonNull(plugin.getCommand("rtp")).setExecutor(new TprandomCommand(plugin));
        Objects.requireNonNull(plugin.getCommand("invsee")).setExecutor(new InvseeCommand(configManager));
        Objects.requireNonNull(plugin.getCommand("hat")).setExecutor(new HatCommand(configManager));
        Objects.requireNonNull(plugin.getCommand("fly")).setExecutor(new FlyCommand(configManager));
        Objects.requireNonNull(plugin.getCommand("gm")).setExecutor(new GmCommand(configManager));
        Objects.requireNonNull(plugin.getCommand("tab")).setExecutor(new TabReloadCommand(plugin, configManager));
        Objects.requireNonNull(plugin.getCommand("eco")).setExecutor(new EcoCommand(plugin, configManager));
        Objects.requireNonNull(plugin.getCommand("speed")).setExecutor(new SpeedCommand(configManager));
        Objects.requireNonNull(plugin.getCommand("money")).setExecutor(new MoneyCommand(plugin, configManager));
        Objects.requireNonNull(plugin.getCommand("otp")).setExecutor(new OtpCommand(plugin.getOtpManager(), configManager));
        Objects.requireNonNull(plugin.getCommand("fandtpa")).setExecutor(new FandTpaCommand(plugin, configManager));
        Objects.requireNonNull(plugin.getCommand("v")).setExecutor(new VanishCommand(plugin,configManager));
        Objects.requireNonNull(plugin.getCommand("hd")).setExecutor(new HologramCommand(configManager));
        Objects.requireNonNull(plugin.getCommand("portalsreload")).setExecutor(new ReloadPortalsCommand(plugin,configManager));
        Objects.requireNonNull(plugin.getCommand("toggleVeinMine")).setExecutor(new VeinMineCommand(plugin));
        Objects.requireNonNull(plugin.getCommand("papihelp")).setExecutor(new papihelpCommand());
        Objects.requireNonNull(plugin.getCommand("more")).setExecutor(new MoreCommand());
    }

    private void registerTabCompleters() {
        Objects.requireNonNull(plugin.getCommand("home")).setTabCompleter(new HomeTabCompleter(plugin));
        Objects.requireNonNull(plugin.getCommand("gm")).setTabCompleter(new GmTabCompleter());
        Objects.requireNonNull(plugin.getCommand("eco")).setTabCompleter(new EcoTabCompleter());
        Objects.requireNonNull(plugin.getCommand("fandtpa")).setTabCompleter(new FandTpaCommand(plugin, configManager));
    }

    private void registerListeners() {
        BackCommand backCommand = new BackCommand(plugin.getLogger(), plugin.getConfigManager());
        Bukkit.getPluginManager().registerEvents(backCommand, plugin);
        Bukkit.getPluginManager().registerEvents(new PlayerChatListener(plugin), plugin);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(otpManager), plugin);
        Bukkit.getPluginManager().registerEvents(new PortalListener(plugin), plugin);
    }
}
