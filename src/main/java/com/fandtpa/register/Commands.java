package com.fandtpa.register;

import com.fandtpa.Main;
import com.fandtpa.commands.*;
import com.fandtpa.commands.TabComplete.EcoTabCompleter;
import com.fandtpa.commands.TabComplete.FandTpaCommand;
import com.fandtpa.commands.TabComplete.GmTabCompleter;
import com.fandtpa.commands.TabComplete.HomeTabCompleter;
import com.fandtpa.manager.ConfigManager;

import java.util.Objects;

public class Commands {
    private final Main plugin;
    private final ConfigManager configManager;

    public Commands(Main plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        registerCommands();
        registerCommandsTabCompleter();
    }

    private void registerCommands() {
        Objects.requireNonNull(this.plugin.getCommand("tpa")).setExecutor(new TpaCommand(configManager));
        Objects.requireNonNull(this.plugin.getCommand("tpahere")).setExecutor(new TpahereCommand(configManager));
        Objects.requireNonNull(this.plugin.getCommand("tpaccept")).setExecutor(new TpacceptCommand(this.plugin, configManager));
        Objects.requireNonNull(this.plugin.getCommand("tpdeny")).setExecutor(new TpdenyCommand(this.plugin, configManager));
        Objects.requireNonNull(this.plugin.getCommand("home")).setExecutor(new HomeCommand(this.plugin, configManager));
        Objects.requireNonNull(this.plugin.getCommand("back")).setExecutor(new BackCommand(this.plugin.getLogger(), configManager));
        Objects.requireNonNull(this.plugin.getCommand("settitle")).setExecutor(new SetTitleCommand(this.plugin, configManager));
        Objects.requireNonNull(this.plugin.getCommand("suicide")).setExecutor(new SuicideCommand(configManager));
        Objects.requireNonNull(this.plugin.getCommand("rtp")).setExecutor(new TprandomCommand(this.plugin));
        Objects.requireNonNull(this.plugin.getCommand("invsee")).setExecutor(new InvseeCommand(configManager));
        Objects.requireNonNull(this.plugin.getCommand("hat")).setExecutor(new HatCommand(configManager));
        Objects.requireNonNull(this.plugin.getCommand("fly")).setExecutor(new FlyCommand(configManager));
        Objects.requireNonNull(this.plugin.getCommand("gm")).setExecutor(new GmCommand(configManager));
        Objects.requireNonNull(this.plugin.getCommand("tab")).setExecutor(new TabReloadCommand(this.plugin, configManager));
        Objects.requireNonNull(this.plugin.getCommand("eco")).setExecutor(new EcoCommand(this.plugin, configManager));
        Objects.requireNonNull(this.plugin.getCommand("speed")).setExecutor(new SpeedCommand(configManager));
        Objects.requireNonNull(this.plugin.getCommand("money")).setExecutor(new MoneyCommand(this.plugin, configManager));
        Objects.requireNonNull(this.plugin.getCommand("otp")).setExecutor(new OtpCommand(this.plugin.getOtpManager(), configManager));
        Objects.requireNonNull(this.plugin.getCommand("fandtpa")).setExecutor(new FandTpaCommand(this.plugin, configManager));
        Objects.requireNonNull(this.plugin.getCommand("v")).setExecutor(new VanishCommand(this.plugin));
        Objects.requireNonNull(this.plugin.getCommand("hd")).setExecutor(new HologramCommand(this.plugin));
        Objects.requireNonNull(this.plugin.getCommand("ftinfo")).setExecutor(new FTInfoCommand(this.plugin));
        Objects.requireNonNull(this.plugin.getCommand("portalsreload")).setExecutor(new ReloadPortalsCommand(this.plugin));
        Objects.requireNonNull(this.plugin.getCommand("toggleVeinMine")).setExecutor(new VeinMineCommand(this.plugin));
        Objects.requireNonNull(this.plugin.getCommand("toggleVeinMine")).setExecutor(new VeinMineCommand(this.plugin));
    }
    private void registerCommandsTabCompleter() {
        Objects.requireNonNull(this.plugin.getCommand("home")).setTabCompleter(new HomeTabCompleter(this.plugin));
        Objects.requireNonNull(this.plugin.getCommand("gm")).setTabCompleter(new GmTabCompleter());
        Objects.requireNonNull(this.plugin.getCommand("eco")).setTabCompleter(new EcoTabCompleter());
        Objects.requireNonNull(this.plugin.getCommand("fandtpa")).setTabCompleter(new FandTpaCommand(this.plugin, configManager));
    }
}
