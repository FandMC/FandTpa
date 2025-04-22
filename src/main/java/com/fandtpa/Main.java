package com.fandtpa;

import com.fandtpa.manager.ConfigManager;
import com.fandtpa.util.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class Main extends JavaPlugin{
    private File homesFile;
    private FileConfiguration homesConfig;
    private File titlesFile;
    private FileConfiguration titlesConfig;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        new Registers(this, configManager);
        new CreateFile(this);
        configManager = new ConfigManager(this);
        configManager.reloadMessages();
        loadConfigurationFiles();
        logToConsole(ChatColor.translateAlternateColorCodes('&',"[Fandtpa] "+ configManager.getMessage("plugin_success")));
    }

    @Override
    public void onDisable() {
        logToConsole(configManager.getMessage("plugin_disabled"));
        saveConfigurationFiles();
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    private void loadConfigurationFiles() {
        homesConfig = YamlConfiguration.loadConfiguration(homesFile);
        titlesConfig = YamlConfiguration.loadConfiguration(titlesFile);
    }

    private void saveConfigurationFiles() {
        saveConfigFile(homesConfig, homesFile);
        saveConfigFile(titlesConfig, titlesFile);
    }

    private void saveConfigFile(FileConfiguration config, File file) {
        try {
            if (config != null && file != null) {
                config.save(file);
            }
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, configManager.getMessage("error_message").replace("{error}", e.getMessage()), e);
        }
    }

    public FileConfiguration getHomesConfig() {
        return homesConfig;
    }

    public void saveHomesConfig() {
        saveConfigFile(homesConfig, homesFile);
    }

    private void logToConsole(String message) {
        getServer().getConsoleSender().sendMessage(message);
    }
}