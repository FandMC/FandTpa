package fand.fandtpa;

import fand.fandtpa.commands.*;
import fand.fandtpa.economy.EcoManager;
import fand.fandtpa.listeners.*;
import fand.fandtpa.tab.TabListUpdater;
import fand.fandtpa.util.ChatColor;
import fand.fandtpa.util.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;

public class Main extends JavaPlugin implements Listener {

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
    private File hologramsFile;
    private FileConfiguration hologramsConfig;
    private File portalsFile;
    private FileConfiguration portalsConfig;
    private Map<Location, PortalData> portalMap = new HashMap<>();

    @Override
    public void onEnable() {
        this.tabConfig = this.getConfig();
        otpManager = new OtpManager();
        checkForTabPlugin();
        configManager = new ConfigManager(this);
        try {
            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
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
            loadHolograms();
            loadTabConfig();
            createDataFolders();
            loadPortals();
            startParticleEffects();
            // 确保语言变量被正确初始化
            language = getConfig().getString("language", "zh_CN");
            getLogger().info("当前语言设置: " + language);

            loadLanguageFiles();  // 释放并加载语言文件

            configManager.reloadMessages();  // 再次加载消息
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

    private void loadHolograms() {
        hologramsFile = new File(getDataFolder(), "holograms.yml");
        if (!hologramsFile.exists()) {
            saveDefaultHologramsConfig(); // 保存带有默认值的配置
        }
        hologramsConfig = YamlConfiguration.loadConfiguration(hologramsFile);

        @NotNull List<Map<?, ?>> hologramsList = hologramsConfig.getMapList("holograms");
        if (hologramsList.isEmpty()) {
            getLogger().warning("Holograms section not found in holograms.yml! Creating a default section.");
            saveDefaultHologramsConfig();
            return;
        }

        for (Map<?, ?> hologramData : hologramsList) {
            String worldName = (String) hologramData.get("world");
            double x = (double) hologramData.get("x");
            double y = (double) hologramData.get("y");
            double z = (double) hologramData.get("z");
            Object textObject = hologramData.get("text");

            if (textObject == null) {
                getLogger().warning("Hologram text is null, skipping...");
                continue;
            }

            // 处理单行或多行文本
            String text;
            if (textObject instanceof String) {
                text = (String) textObject;
            } else if (textObject instanceof List) {
                List<String> textList = (List<String>) textObject;
                text = String.join("\n", textList); // 多行文本使用换行符拼接
            } else {
                getLogger().warning("Hologram text format is invalid, skipping...");
                continue;
            }

            if (text.isEmpty()) {
                getLogger().warning("Hologram text is empty, skipping...");
                continue;
            }

            World world = Bukkit.getWorld(worldName);
            if (world != null) {
                Location location = new Location(world, x, y, z);
                spawnHologram(location, text);
            } else {
                getLogger().warning("World '" + worldName + "' not found for hologram at " + x + ", " + y + ", " + z);
            }
        }
    }



    private void saveDefaultHologramsConfig() {
        hologramsConfig = YamlConfiguration.loadConfiguration(hologramsFile);
        List<Map<String, Object>> defaultHolograms = new ArrayList<>();

        Map<String, Object> exampleHologram = new HashMap<>();
        exampleHologram.put("world", "world");
        exampleHologram.put("x", 100.5);
        exampleHologram.put("y", 65.0);
        exampleHologram.put("z", 200.5);
        exampleHologram.put("text", "欢迎来到服务器！");
        defaultHolograms.add(exampleHologram);

        hologramsConfig.set("holograms", defaultHolograms);
        try {
            hologramsConfig.save(hologramsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void reloadHolograms() {
        // 清除所有现有的 ArmorStand（实现清除逻辑）
        Bukkit.getWorlds().forEach(world ->
                world.getEntitiesByClass(ArmorStand.class).stream()
                        .filter(armorStand -> armorStand.isMarker() && armorStand.getCustomName() != null)
                        .forEach(ArmorStand::remove)
        );

        // 重新加载悬浮字
        loadHolograms();
    }

    private void spawnHologram(Location location, String text) {
        String[] lines = text.split("\n");
        double lineSpacing = 0.25; // 每行之间的间距，调整这个值来改变行之间的距离

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];

            ArmorStand armorStand = location.getWorld().spawn(location.clone().add(0, -i * lineSpacing, 0), ArmorStand.class);
            armorStand.setGravity(false);
            armorStand.setVisible(false);
            armorStand.setCustomName(ChatColor.translateAlternateColorCodes('&', line));
            armorStand.setCustomNameVisible(true);
            armorStand.setMarker(true);
        }
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
                Object economy = getProviderMethod.invoke(rsp); // 获取经济服务提供者
                return economy != null;
            }

            return false;
        } catch (Exception e) {
            getLogger().log(Level.WARNING, "Vault 未找到，经济功能禁用: " + e.getMessage());
            return false;
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

    private void loadLanguageFiles() {
        File langFolder = new File(getDataFolder(), "lang");
        if (!langFolder.exists()) {
            langFolder.mkdirs();
        }

        // 自动释放语言文件
        String[] supportedLanguages = {"zh_CN.yml", "en_us.yml"};
        for (String langFileName : supportedLanguages) {
            File langFile = new File(langFolder, langFileName);
            if (!langFile.exists()) {
                saveResource("lang/" + langFileName, false);
                getLogger().info("释放语言文件: " + langFileName);
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
                    getLogger().info(fileName + " 文件创建成功: " + file.getPath());
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

    public void toggleVanish(org.bukkit.entity.Player player) {
        if (player.hasMetadata("vanished")) {
            // 解除隐身状态
            player.removeMetadata("vanished", this);
            Bukkit.getOnlinePlayers().forEach(p -> p.showPlayer(this, player));
            player.sendMessage(ChatColor.GREEN + "你现在已取消隐身！");
        } else {
            // 设置隐身状态
            player.setMetadata("vanished", new org.bukkit.metadata.FixedMetadataValue(this, true));
            Bukkit.getOnlinePlayers().forEach(p -> p.hidePlayer(this, player));
            player.sendMessage(ChatColor.GREEN + "你现在已隐身！");
        }
    }

    private void registerCommands() {
        Objects.requireNonNull(this.getCommand("tpa")).setExecutor(new TpaCommand(configManager));
        Objects.requireNonNull(this.getCommand("tpahere")).setExecutor(new TpahereCommand(configManager));
        Objects.requireNonNull(this.getCommand("tpaccept")).setExecutor(new TpacceptCommand(this, configManager));
        Objects.requireNonNull(this.getCommand("tpdeny")).setExecutor(new TpdenyCommand(this, configManager));
        Objects.requireNonNull(this.getCommand("home")).setExecutor(new HomeCommand(this, configManager));
        Objects.requireNonNull(this.getCommand("home")).setTabCompleter(new HomeTabCompleter(this));
        Objects.requireNonNull(this.getCommand("back")).setExecutor(new BackCommand(this.getLogger(), configManager));
        Objects.requireNonNull(this.getCommand("settitle")).setExecutor(new SetTitleCommand(this, configManager));
        Objects.requireNonNull(this.getCommand("suicide")).setExecutor(new SuicideCommand(configManager));
        Objects.requireNonNull(this.getCommand("rtp")).setExecutor(new TprandomCommand(this));
        Objects.requireNonNull(this.getCommand("invsee")).setExecutor(new InvseeCommand(configManager));
        Objects.requireNonNull(this.getCommand("hat")).setExecutor(new HatCommand(configManager));
        Objects.requireNonNull(this.getCommand("fly")).setExecutor(new FlyCommand(configManager));
        Objects.requireNonNull(this.getCommand("gm")).setExecutor(new GmCommand(configManager));
        Objects.requireNonNull(this.getCommand("gm")).setTabCompleter(new GmTabCompleter());
        Objects.requireNonNull(this.getCommand("tab")).setExecutor(new TabReloadCommand(this, configManager));
        Objects.requireNonNull(this.getCommand("eco")).setExecutor(new EcoCommand(this, configManager));
        Objects.requireNonNull(this.getCommand("eco")).setTabCompleter(new EcoTabCompleter());
        Objects.requireNonNull(this.getCommand("speed")).setExecutor(new SpeedCommand(configManager));
        Objects.requireNonNull(this.getCommand("money")).setExecutor(new MoneyCommand(this, configManager));
        Objects.requireNonNull(this.getCommand("otp")).setExecutor(new OtpCommand(otpManager, configManager));
        Objects.requireNonNull(this.getCommand("fandtpa")).setExecutor(new FandTpaCommand(this, configManager));
        Objects.requireNonNull(this.getCommand("fandtpa")).setTabCompleter(new FandTpaCommand(this, configManager));
        Objects.requireNonNull(this.getCommand("v")).setExecutor(new VanishCommand(this));
        Objects.requireNonNull(this.getCommand("hd")).setExecutor(new HologramCommand(this));
        Objects.requireNonNull(this.getCommand("fserver")).setExecutor(new FServerCommand());
        Objects.requireNonNull(this.getCommand("ftinfo")).setExecutor(new FTInfoCommand(this));
    }

    private void registerListeners() {
        BackCommand backCommand = new BackCommand(getLogger(), new ConfigManager(this));
        Bukkit.getPluginManager().registerEvents(new PlayerChatListener(this), this);
        getServer().getPluginManager().registerEvents(backCommand, this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(otpManager), this);
        getServer().getPluginManager().registerEvents(new PortalListener(this), this);
    }

    private void logToConsole(String message) {
        getServer().getConsoleSender().sendMessage(message);
    }
    public void loadPortals() {
        portalsFile = new File(getDataFolder(), "portals.yml");
        if (!portalsFile.exists()) {
            saveResource("portals.yml", false);
        }
        portalsConfig = YamlConfiguration.loadConfiguration(portalsFile);
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

}