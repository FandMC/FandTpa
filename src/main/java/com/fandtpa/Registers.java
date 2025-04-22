package com.fandtpa;

import com.fandtpa.commands.*;
import com.fandtpa.commands.TabComplete.*;
import com.fandtpa.manager.*;
import org.bukkit.Bukkit;

import java.util.Objects;

public class Registers {
    private final Main plugin;
    private final ConfigManager configManager;

    public Registers(Main plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
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
        Objects.requireNonNull(plugin.getCommand("rtp")).setExecutor(new TprandomCommand(plugin));
        Objects.requireNonNull(plugin.getCommand("hat")).setExecutor(new HatCommand(configManager));
        Objects.requireNonNull(plugin.getCommand("fandtpa")).setExecutor(new FandTpaCommand(plugin, configManager));
    }

    private void registerTabCompleters() {
        Objects.requireNonNull(plugin.getCommand("home")).setTabCompleter(new HomeTabCompleter(plugin));
        Objects.requireNonNull(plugin.getCommand("fandtpa")).setTabCompleter(new FandTpaCommand(plugin, configManager));
    }

    private void registerListeners() {
        BackCommand backCommand = new BackCommand(plugin.getLogger(), plugin.getConfigManager());
        Bukkit.getPluginManager().registerEvents(backCommand, plugin);
    }
}
