package com.fandtpa.util;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class CreateFile {
    private final Logger logger;
    private final File dataFolder;

    public CreateFile(JavaPlugin plugin) {
        this.logger = plugin.getLogger();
        this.dataFolder = plugin.getDataFolder();
        createDataFolders();
        createHomeConfig();
    }

    public @NotNull File getDataFolder() {
        return this.dataFolder;
    }

    public @NotNull Logger getLogger() {
        return this.logger;
    }

    public void createFile(String fileName) {
        File file = new File(getDataFolder(), fileName);

        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    getLogger().info("文件 " + fileName + " 创建成功！");
                } else {
                    getLogger().severe("文件 " + fileName + " 创建失败！");
                }
            } catch (IOException e) {
                getLogger().severe("创建文件 " + fileName + " 时发生异常: " + e.getMessage());
            }
        } else {
            getLogger().info("文件 " + fileName + " 已经存在。");
        }
    }

    private void createDataFolders() {
        if (!getDataFolder().exists()) {
            if (getDataFolder().mkdirs()) {
                getLogger().info("插件主数据文件夹创建成功: " + getDataFolder().getPath());
            } else {
                getLogger().severe("插件主数据文件夹创建失败: " + getDataFolder().getPath());
            }
        }

        File langFolder = new File(getDataFolder(), "lang");
        if (!langFolder.exists()) {
            if (langFolder.mkdirs()) {
                getLogger().info("lang 文件夹创建成功: " + langFolder.getPath());
            } else {
                getLogger().severe("lang 文件夹创建失败: " + langFolder.getPath());
            }
        }
    }

    private void createHomeConfig() {
        createFile("homes.yml");
    }
}
