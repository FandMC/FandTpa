package com.fandtpa;

import com.fandtpa.manager.ConfigManager;
import com.fandtpa.manager.Holograms;
import com.fandtpa.manager.EcoManager;
import com.fandtpa.manager.OtpManager;
import com.fandtpa.register.*;
import com.fandtpa.tab.TabListUpdater;
import com.fandtpa.util.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;

public class Main extends JavaPlugin implements Listener {
    public String FandMCVersion = "2.2";
    public FileConfiguration tabConfig;
    private String language;
    private File homesFile;
    private FileConfiguration homesConfig;
    private File titlesFile;
    private FileConfiguration titlesConfig;
    private ConfigManager configManager;
    private boolean tabFunctionEnabled = true;
    private EcoManager ecoManager;
    private OtpManager otpManager;
    private final Map<Location, PortalData> portalMap = new HashMap<>();
    Holograms holograms;
    private CreateFile createFileUtil;
    private int maxVeinMineBlocks;

    @Override
    public void onEnable() {
        try {
            a();
            start();
            logToConsole(ChatColor.translateAlternateColorCodes('&',"[Fandtpa] "+ configManager.getMessage("plugin_success")));
            a();
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, configManager.getMessage("error_message").replace("{error}", e.getMessage()), e);
        }
    }

    private void start() {
        maxVeinMineBlocks = getConfig().getInt("max_vein_mine_blocks", 100); // 默认值为100
        checkupdate();
        checkForTabPlugin();
        this.tabConfig = this.getConfig();
        otpManager = new OtpManager();
        configManager = new ConfigManager(this);
        holograms = new Holograms(this);
        PluginsNo();
        tabEnabled();
        String dbPath = getDataFolder().getAbsolutePath() + "/economy.db";
        ecoManager = new EcoManager(dbPath);
        holograms.loadHolograms();
        loadTabConfig();
        createDataFolders();
        createHomeConfig();
        createTitlesConfig();
        loadPortals();
        startParticleEffects();
        language();
        configManager.reloadMessages();
        loadConfigurationFiles();
        new Commands(this, configManager);
        new Listeners(this, otpManager);
    }

    private void createTitlesConfig() {
        titlesFile = createFileUtil.createFile("titles.yml");
    }

    private void createHomeConfig() {
        homesFile = createFileUtil.createFile("homes.yml");
    }

    private void a(){
        getLogger().info("------------------------------------");
    }

    private void checkupdate() {
        getLogger().info("当前版本为: " + FandMCVersion);
        CountDownLatch latch = new CountDownLatch(1);

        CheckUpdate updater = new CheckUpdate(this);
        new Thread(() -> {
            updater.check(FandMCVersion);
            latch.countDown();
        }).start();

        try {
            latch.await();
        } catch (InterruptedException ignored) {
        }
    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        otpManager.saveLogoutLocation(event.getPlayer());
    }

    private void language(){
        language = getConfig().getString("language", "zh_CN");
        getLogger().info("当前语言设置: " + language);
        loadLanguageFiles();  // 释放并加载语言文件
    }

    private void tabEnabled(){
        if (tabFunctionEnabled) {
            int time = tabConfig.getInt("time", 20); // 从 tab.yml 获取 time 配置项，默认为 20
            new TabListUpdater(this).runTaskTimer(this, 0L, time);
        }
    }

    private void PluginsNo(){
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            getLogger().info("检测到PlaceholderAPI，已启用PlaceholderAPI支持。");
        } else {
            getLogger().info("未找到PlaceholderAPI，计分板将会启用内置变量。");
        }
        if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
            getLogger().info("检测到 Vault 插件，已启用经济支持。");
        } else {
            getLogger().info("未检测到 Vault 插件，经济功能将无法使用。");
        }
    }

    public EcoManager getEcoManager() {
        return ecoManager;
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

    private void loadLanguageFiles() {
        File langFolder = new File(getDataFolder(), "lang");
        if (!langFolder.exists()) {
            if (langFolder.mkdirs()) {
                getLogger().info("创建语言文件夹：" + langFolder.getAbsolutePath());
            } else {
                getLogger().severe("无法创建语言文件夹：" + langFolder.getAbsolutePath());
            }
        }

        String[] supportedLanguages = {"zh_CN.yml", "en_us.yml"};
        for (String langFileName : supportedLanguages) {
            File langFile = new File(langFolder, langFileName);
            if (!langFile.exists()) {
                saveResource("lang/" + langFileName, false);
                getLogger().info("释放语言文件: " + langFileName);

                // 检查文件是否成功创建
                if (!langFile.exists()) {
                    getLogger().severe("释放语言文件失败: " + langFileName);
                }
            }
        }

        // 加载当前语言文件
        if (language == null) {
            getLogger().severe("语言未设置，加载默认语言文件: zh_CN.yml");
            language = "zh_CN";
        }

        File languageFile = new File(langFolder, language + ".yml");
        if (languageFile.exists()) {
            getLogger().info("加载语言文件: " + languageFile.getAbsolutePath());
        } else {
            getLogger().severe("语言文件加载失败: " + languageFile.getAbsolutePath());
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
        createFileUtil = new CreateFile(getLogger(), getDataFolder());
        // 创建插件的主数据文件夹
        if (!getDataFolder().exists() && !getDataFolder().mkdirs()) {
            getLogger().severe("插件主数据文件夹创建失败: " + getDataFolder().getPath());
        }
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

    public void toggleVanish(Player player) {
        if (player.hasMetadata("vanished")) {
            // 解除隐身状态
            player.removeMetadata("vanished", this);
            Bukkit.getOnlinePlayers().forEach(p -> p.showPlayer(this, player));
            player.sendMessage(ChatColor.GREEN + "你现在已取消隐身！");
        } else {
            // 设置隐身状态
            player.setMetadata("vanished", new FixedMetadataValue(this, true));
            Bukkit.getOnlinePlayers().forEach(p -> p.hidePlayer(this, player));
            player.sendMessage(ChatColor.GREEN + "你现在已隐身！");
        }
    }

    private void logToConsole(String message) {
        getServer().getConsoleSender().sendMessage(message);
    }
    public OtpManager getOtpManager() {
        return otpManager;
    }

    public void loadPortals() {
        File portalsFile = new File(getDataFolder(), "portals.yml");
        if (!portalsFile.exists()) {
            saveResource("portals.yml", false);
        }
        FileConfiguration portalsConfig = YamlConfiguration.loadConfiguration(portalsFile);
        portalMap.clear();

        ConfigurationSection worldsSection = portalsConfig.getConfigurationSection("portals");
        if (worldsSection != null) {
            for (String worldName : worldsSection.getKeys(false)) {
                World world = Bukkit.getWorld(worldName);
                if (world != null) {
                    ConfigurationSection worldSection = worldsSection.getConfigurationSection(worldName);
                    if (worldSection != null) {
                        for (String portalKey : worldSection.getKeys(false)) {
                            ConfigurationSection portalSection = worldSection.getConfigurationSection(portalKey);
                            if (portalSection != null) {
                                double x1 = portalSection.getDouble("region.x1");
                                double y1 = portalSection.getDouble("region.y1");
                                double z1 = portalSection.getDouble("region.z1");
                                double x2 = portalSection.getDouble("region.x2");
                                double y2 = portalSection.getDouble("region.y2");
                                double z2 = portalSection.getDouble("region.z2");
                                String command = portalSection.getString("command");
                                String particleEffect = portalSection.getString("particles", "");
                                Location corner1 = new Location(world, x1, y1, z1);
                                Location corner2 = new Location(world, x2, y2, z2);
                                PortalData portalData = new PortalData(corner1, corner2, command, particleEffect);
                                portalMap.put(corner1, portalData);
                            }
                        }
                    }
                }
            }
        }
    }

    public void startParticleEffects() {
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (PortalData portal : portalMap.values()) {
                if (portal.hasParticles()) {
                    try {
                        Particle particle = Particle.valueOf(portal.getParticleEffect().toUpperCase());
                        generatePortalParticles(portal, particle);
                    } catch (IllegalArgumentException e) {
                        getLogger().warning("Invalid particle effect: " + portal.getParticleEffect() + ". Disabling particles.");
                        portal.disableParticles();
                    }
                }
            }
        }, 0L, 20L); // 每秒钟执行一次
    }

    private void generatePortalParticles(PortalData portal, Particle particle) {
        World world = portal.getCorner1().getWorld();

        // 获取传送门区域的最小和最大坐标
        int minX = Math.min(portal.getCorner1().getBlockX(), portal.getCorner2().getBlockX());
        int maxX = Math.max(portal.getCorner1().getBlockX(), portal.getCorner2().getBlockX());
        int minY = Math.min(portal.getCorner1().getBlockY(), portal.getCorner2().getBlockY());
        int maxY = Math.max(portal.getCorner1().getBlockY(), portal.getCorner2().getBlockY());
        int minZ = Math.min(portal.getCorner1().getBlockZ(), portal.getCorner2().getBlockZ());
        int maxZ = Math.max(portal.getCorner1().getBlockZ(), portal.getCorner2().getBlockZ());

        // 在 X 方向的边缘生成粒子
        for (int x = minX; x <= maxX; x++) {
            spawnParticleAt(world, particle, x, minY, minZ);
            spawnParticleAt(world, particle, x, minY, maxZ);
            spawnParticleAt(world, particle, x, maxY, minZ);
            spawnParticleAt(world, particle, x, maxY, maxZ);
        }

        // 在 Y 方向的边缘生成粒子
        for (int y = minY; y <= maxY; y++) {
            spawnParticleAt(world, particle, minX, y, minZ);
            spawnParticleAt(world, particle, minX, y, maxZ);
            spawnParticleAt(world, particle, maxX, y, minZ);
            spawnParticleAt(world, particle, maxX, y, maxZ);
        }

        // 在 Z 方向的边缘生成粒子
        for (int z = minZ; z <= maxZ; z++) {
            spawnParticleAt(world, particle, minX, minY, z);
            spawnParticleAt(world, particle, minX, maxY, z);
            spawnParticleAt(world, particle, maxX, minY, z);
            spawnParticleAt(world, particle, maxX, maxY, z);
        }
    }

    private void spawnParticleAt(World world, Particle particle, int x, int y, int z) {
        Location loc = new Location(world, x, y, z);
        world.spawnParticle(particle, loc, 1, 0, 0, 0, 0);
    }

    public void executePortalCommand(Player player, PortalData portalData) {
        String command = portalData.getCommand().replace("{player}", player.getName());
        getServer().dispatchCommand(getServer().getConsoleSender(), command);

        if (portalData.hasParticles()) {
            try {
                Particle particle = Particle.valueOf(portalData.getParticleEffect().toUpperCase());
                Location center = portalData.getCorner1().toVector().getMidpoint(portalData.getCorner2().toVector()).toLocation(portalData.getCorner1().getWorld());
                player.getWorld().spawnParticle(particle, center, 30);
            } catch (IllegalArgumentException e) {
                getLogger().warning("Invalid particle effect: " + portalData.getParticleEffect() + ". Disabling particles.");
                portalData.disableParticles();
            }
        }
    }

    public Map<Location, PortalData> getPortalMap() {
        return portalMap;
    }

    public int getMaxVeinMineBlocks() {
        return maxVeinMineBlocks;
    }
}