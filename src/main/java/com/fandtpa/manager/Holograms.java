package com.fandtpa.manager;

import com.fandtpa.Main;
import com.fandtpa.util.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Holograms {
    private final Main plugin;
    private FileConfiguration hologramsConfig;
    private final File hologramsFile;
    private static final Logger LOGGER = Logger.getLogger(Holograms.class.getName());

    public Holograms(Main plugin) {
        this.plugin = plugin;
        hologramsFile = new File(plugin.getDataFolder(), "holograms.yml");
        if (!hologramsFile.exists()) {
            saveDefaultHologramsConfig(); // 保存带有默认值的配置
        }
        hologramsConfig = YamlConfiguration.loadConfiguration(hologramsFile);
    }

    public void loadHolograms() {
        List<Map<?, ?>> hologramsList = hologramsConfig.getMapList("holograms");
        if (hologramsList.isEmpty()) {
            saveDefaultHologramsConfig();
            plugin.getLogger().warning("Holograms section not found in holograms.yml! Creating a default section.");
            return;
        }

        for (Map<?, ?> hologramData : hologramsList) {
            String worldName = (String) hologramData.get("world");
            double x = (double) hologramData.get("x");
            double y = (double) hologramData.get("y");
            double z = (double) hologramData.get("z");
            Object textObject = hologramData.get("text");

            if (textObject == null) {
                plugin.getLogger().warning("Hologram text is null, skipping...");
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
                plugin.getLogger().warning("Hologram text format is invalid, skipping...");
                continue;
            }

            if (text.isEmpty()) {
                plugin.getLogger().warning("Hologram text is empty, skipping...");
                continue;
            }

            World world = Bukkit.getWorld(worldName);
            if (world != null) {
                Location location = new Location(world, x, y, z);
                spawnHologram(location, text);
            } else {
                plugin.getLogger().warning("World '" + worldName + "' not found for hologram at " + x + ", " + y + ", " + z);
            }
        }
    }

    public void saveDefaultHologramsConfig() {
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
            LOGGER.log(Level.SEVERE, "发生IO异常", e);
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

    public void spawnHologram(Location location, String text) {
        String[] lines = text.split("\n");
        double lineSpacing = 0.25; // 每行之间的间距

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
}
