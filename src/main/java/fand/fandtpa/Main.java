package fand.fandtpa;

import fand.fandtpa.commands.*;
import fand.fandtpa.economy.EcoManager;
import fand.fandtpa.listeners.*;
import fand.fandtpa.tab.TabListUpdater;
import fand.fandtpa.util.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;

public class Main extends JavaPlugin {

    public FileConfiguration tabConfig;
    private FileConfiguration languageConfig;
    private String language;
    private File homesFile;
    private FileConfiguration homesConfig;
    private File titlesFile;
    private FileConfiguration titlesConfig;
    private final HashMap<UUID, Location> deathLocations = new HashMap<>();
    private ConfigManager configManager;
    private boolean tabFunctionEnabled = true;
    private boolean papiEnabled = false;
    private Object economy = null;
    private EcoManager ecoManager;
    private OtpManager otpManager;

    @Override
    public void onEnable() {
        otpManager = new OtpManager();
        checkForTabPlugin();
        configManager = new ConfigManager(this);
        saveDefaultConfig();
        language = getConfig().getString("language", "zh_CN"); // 设置默认语言为 zh_CN
        loadLanguageFile();


        try {
            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                papiEnabled = true;
                getLogger().info("检测到PlaceholderAPI，已启用PlaceholderAPI支持。");
            } else {
                getLogger().info("未找到PlaceholderAPI，将在没有PlaceholderAPI支持的情况下运行。");
            }
            if (setupEconomy()) {
                getLogger().info("检测到 Vault 插件，已启用经济支持。");
            } else {
                getLogger().warning("未检测到 Vault 插件，经济功能将无法使用。");
            }

            if (tabFunctionEnabled) {
                int time = 20;
                new TabListUpdater(this).runTaskTimer(this, 0L, time);
            }
            String dbPath = getDataFolder().getAbsolutePath() + "/economy.db";
            ecoManager = new EcoManager(dbPath);
            loadTabConfig();
            createDataFolders();
            loadConfigurationFiles();
            registerCommands();
            registerListeners();
            logToConsole(configManager.getMessage("plugin_success"));
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, configManager.getMessage("error_message").replace("{error}", e.getMessage()), e);
        }
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        otpManager.saveLogoutLocation(event.getPlayer());
    }
    public EcoManager getEcoManager() {
        return ecoManager;
    }
    private boolean setupEconomy() {
        try {
            if (getServer().getPluginManager().getPlugin("Vault") == null) {
                return false;
            }

            // 通过反射来获取 Vault 的经济服务
            Class<?> rspClass = Class.forName("org.bukkit.plugin.RegisteredServiceProvider");
            Method getServicesManagerMethod = getServer().getClass().getMethod("getServicesManager");
            Object servicesManager = getServicesManagerMethod.invoke(getServer());

            Method getRegistrationMethod = servicesManager.getClass().getMethod("getRegistration", Class.class);
            Object rsp = getRegistrationMethod.invoke(servicesManager, Class.forName("net.milkbowl.vault.economy.Economy"));

            if (rsp != null) {
                Method getProviderMethod = rspClass.getMethod("getProvider");
                economy = getProviderMethod.invoke(rsp); // 获取经济服务提供者
                return economy != null;
            }

            return false;
        } catch (Exception e) {
            getLogger().log(Level.WARNING, "Vault 未找到，经济功能禁用: " + e.getMessage());
            return false;
        }
    }

    public Object getEconomy() {
        return economy;
    }
    public String replacePlaceholders(Player player, String text) {
        if (papiEnabled) {
            return me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, text);
        } else {
            return text;
        }
    }
    private void checkForTabPlugin() {
        Plugin tabPlugin = Bukkit.getPluginManager().getPlugin("TAB");
        if (tabPlugin != null && tabPlugin.isEnabled()) {
            tabFunctionEnabled = false;
        }
    }
    @Override
    public void onDisable() {
        logToConsole(configManager.getMessage("plugin_disabled"));
        saveConfigurationFiles();
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
    private void loadLanguageFile() {
        // 确保 lang 文件夹存在
        File langFolder = new File(getDataFolder(), "lang");
        if (!langFolder.exists()) {
            langFolder.mkdirs();
        }

        // 确保所有支持的语言文件都被释放
        String[] supportedLanguages = {"zh_CN", "en_us"};
        for (String lang : supportedLanguages) {
            File langFile = new File(langFolder, lang + ".yml");
            if (!langFile.exists()) {
                saveResource("lang/" + lang + ".yml", false);
                getLogger().info("释放语言文件: " + lang + ".yml");
            }
        }

        // 加载当前配置选择的语言文件
        File languageFile = new File(langFolder, language + ".yml");
        getLogger().info("加载语言文件: " + languageFile.getAbsolutePath());

        languageConfig = YamlConfiguration.loadConfiguration(languageFile);

        if (languageConfig == null) {
            getLogger().severe("语言文件加载失败");
        } else {
            getLogger().info("语言文件加载成功");
        }
    }


    public void loadTabConfig() {
        File tabFile = new File(getDataFolder(), "tab.yml");
        if (!tabFile.exists()) {
            saveResource("tab.yml", false);
        }
        tabConfig = YamlConfiguration.loadConfiguration(tabFile);
    }

    private void createDataFolders() {
        // 创建插件的主数据文件夹
        if (!getDataFolder().exists() && !getDataFolder().mkdirs()) {
            getLogger().severe("插件主数据文件夹创建失败: " + getDataFolder().getPath());
            return;
        }

        // 创建 homes.yml 和 titles.yml 文件
        homesFile = createFile("homes.yml");
        titlesFile = createFile("titles.yml");
    }

    private File createFile(String fileName) {
        File file = new File(getDataFolder(), fileName);
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                } else {
                    getLogger().severe(fileName + " 文件创建失败: " + file.getPath());
                }
            } catch (IOException e) {
                getLogger().log(Level.SEVERE, "创建 " + fileName + " 文件时发生错误: " + e.getMessage(), e);
            }
        }
        return file;
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

    public FileConfiguration getTitlesConfig() {
        return titlesConfig;
    }

    public void saveTitlesConfig() {
        saveConfigFile(titlesConfig, titlesFile);
    }

    private void registerCommands() {
        EcoCommand ecoCommand = new EcoCommand(this,configManager);
        Objects.requireNonNull(this.getCommand("tpa")).setExecutor(new TpaCommand(this,configManager));
        Objects.requireNonNull(this.getCommand("tpahere")).setExecutor(new TpahereCommand(this,configManager));
        Objects.requireNonNull(this.getCommand("tpaccept")).setExecutor(new TpacceptCommand(this,configManager));
        Objects.requireNonNull(this.getCommand("tpdeny")).setExecutor(new TpdenyCommand(this,configManager));
        Objects.requireNonNull(this.getCommand("home")).setExecutor(new HomeCommand(this,configManager));
        Objects.requireNonNull(this.getCommand("home")).setTabCompleter(new HomeTabCompleter(this));
        Objects.requireNonNull(this.getCommand("back")).setExecutor(new BackCommand(this.getLogger(), configManager));
        Objects.requireNonNull(this.getCommand("settitle")).setExecutor(new SetTitleCommand(this,configManager));
        Objects.requireNonNull(this.getCommand("suicide")).setExecutor(new SuicideCommand(configManager));
        Objects.requireNonNull(this.getCommand("rtp")).setExecutor(new TprandomCommand(this));
        Objects.requireNonNull(this.getCommand("invsee")).setExecutor(new InvseeCommand(configManager));
        Objects.requireNonNull(this.getCommand("hat")).setExecutor(new HatCommand(configManager));
        Objects.requireNonNull(this.getCommand("fly")).setExecutor(new FlyCommand(configManager));
        Objects.requireNonNull(this.getCommand("gm")).setExecutor(new GmCommand(configManager));
        Objects.requireNonNull(this.getCommand("gm")).setTabCompleter(new GmTabCompleter());
        Objects.requireNonNull(this.getCommand("tab")).setExecutor(new TabReloadCommand(this,configManager));
        Objects.requireNonNull(this.getCommand("eco")).setExecutor(new EcoCommand(this,configManager));
        Objects.requireNonNull(this.getCommand("eco")).setTabCompleter(new EcoTabCompleter());
        Objects.requireNonNull(this.getCommand("speed")).setExecutor(new SpeedCommand(configManager));
        Objects.requireNonNull(this.getCommand("money")).setExecutor(new MoneyCommand(this,configManager));
        Objects.requireNonNull(this.getCommand("otp")).setExecutor(new OtpCommand(otpManager,configManager));
    }

    private void registerListeners() {
        BackCommand backCommand = new BackCommand(getLogger(), new ConfigManager(this));
        Bukkit.getPluginManager().registerEvents(new PlayerChatListener(this), this);
        getServer().getPluginManager().registerEvents(backCommand, this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(otpManager), this);
    }

    private void logToConsole(String message) {
        getServer().getConsoleSender().sendMessage(message);
    }
}
