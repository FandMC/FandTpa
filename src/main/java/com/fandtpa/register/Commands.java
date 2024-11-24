package com.fandtpa.register;

import com.fandtpa.Main;
import com.fandtpa.commands.*;
import com.fandtpa.commands.TabComplete.EcoTabCompleter;
import com.fandtpa.commands.TabComplete.FandTpaCommand;
import com.fandtpa.commands.TabComplete.GmTabCompleter;
import com.fandtpa.commands.TabComplete.HomeTabCompleter;
import com.fandtpa.manager.ConfigManager;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;

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
        registerCommand("tpa", new TpaCommand(configManager));
        registerCommand("tpahere", new TpahereCommand(configManager));
        registerCommand("tpaccept", new TpacceptCommand(this.plugin, configManager));
        registerCommand("tpdeny", new TpdenyCommand(this.plugin, configManager));
        registerCommand("home", new HomeCommand(this.plugin, configManager));
        registerCommand("back", new BackCommand(this.plugin.getLogger(), configManager));
        registerCommand("settitle", new SetTitleCommand(this.plugin, configManager));
        registerCommand("suicide", new SuicideCommand(configManager));
        registerCommand("rtp", new TprandomCommand(this.plugin));
        registerCommand("invsee", new InvseeCommand(configManager));
        registerCommand("hat", new HatCommand(configManager));
        registerCommand("fly", new FlyCommand(configManager));
        registerCommand("gm", new GmCommand(configManager));
        registerCommand("tab", new TabReloadCommand(this.plugin, configManager));
        registerCommand("eco", new EcoCommand(this.plugin, configManager));
        registerCommand("speed", new SpeedCommand(configManager));
        registerCommand("money", new MoneyCommand(this.plugin, configManager));
        registerCommand("otp", new OtpCommand(this.plugin.getOtpManager(), configManager));
        registerCommand("fandtpa", new FandTpaCommand(this.plugin, configManager));
        registerCommand("v", new VanishCommand(this.plugin));
        registerCommand("hd", new HologramCommand(this.plugin));
        registerCommand("ftinfo", new FTInfoCommand(this.plugin));
        registerCommand("portalsreload", new ReloadPortalsCommand(this.plugin));
        registerCommand("toggleVeinMine", new VeinMineCommand(this.plugin));
        registerCommand("papihelp", new papihelpCommand(this.plugin));
    }
    private void registerCommand(String commandName, CommandExecutor executor) {
        PluginCommand command = this.plugin.getCommand(commandName);
        if (command == null) {
            this.plugin.getLogger().warning("指令 '" + commandName + "' 未在 plugin.yml 中定义，跳过注册。");
        } else {
            command.setExecutor(executor);
            this.plugin.getLogger().info("指令 '" + commandName + "' 注册成功。");
        }
    }
    private void registerCommandsTabCompleter() {
        Objects.requireNonNull(this.plugin.getCommand("home")).setTabCompleter(new HomeTabCompleter(this.plugin));
        Objects.requireNonNull(this.plugin.getCommand("gm")).setTabCompleter(new GmTabCompleter());
        Objects.requireNonNull(this.plugin.getCommand("eco")).setTabCompleter(new EcoTabCompleter());
        Objects.requireNonNull(this.plugin.getCommand("fandtpa")).setTabCompleter(new FandTpaCommand(this.plugin, configManager));
    }
}
