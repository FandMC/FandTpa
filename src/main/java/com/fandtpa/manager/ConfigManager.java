package com.fandtpa.manager;

import com.fandtpa.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ConfigManager {

    private final Main plugin;
    private final Logger logger;
    private final Map<String, String> messages = new HashMap<>();
    private String language;
    public void reloadMessages() {
        messages.clear();
        loadMessages();
    }
    public ConfigManager(Main plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        loadConfig();
        loadMessages();
    }

    private void loadConfig() {
        plugin.saveDefaultConfig();
        language = plugin.getConfig().getString("language", "zh_CN");
    }

    private void loadMessages() {
        File langFolder = new File(plugin.getDataFolder(), "lang");
        if (!langFolder.exists()) {
            if (langFolder.mkdirs()) {
                plugin.getLogger().info("创建语言文件夹：" + langFolder.getAbsolutePath());
            } else {
                plugin.getLogger().severe("无法创建语言文件夹：" + langFolder.getAbsolutePath());
            }
        }

        File langFile = new File(langFolder, language + ".yml");
        if (!langFile.exists()) {
            langFile = new File(langFolder, "zh_CN.yml");
        }

        FileConfiguration langConfig = YamlConfiguration.loadConfiguration(langFile);

        for (String key : langConfig.getKeys(false)) {
            messages.put(key, langConfig.getString(key));
        }
    }

    public String getMessage(String key) {
        return messages.getOrDefault(key, "§c[Missing language key: " + key + "]");
    }

    public Logger getLogger() {
        return logger;
    }
}
