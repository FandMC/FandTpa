package com.fandtpa.util;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class CreateFile {
    private final Logger logger;
    private final File dataFolder;

    // 构造函数，初始化 logger 和 dataFolder，并自动调用文件创建方法
    public CreateFile(JavaPlugin plugin) {
        this.logger = plugin.getLogger();         // 使用插件的 logger
        this.dataFolder = plugin.getDataFolder(); // 使用插件的 dataFolder
        createDataFolders(); // 初始化数据文件夹

        // 自动调用创建配置文件的方法
        createTitlesConfig();
        createHomeConfig();
    }

    public @NotNull File getDataFolder() {
        return this.dataFolder;
    }

    public @NotNull Logger getLogger() {
        return this.logger;
    }

    // 创建文件
    public void createFile(String fileName) {
        File file = new File(getDataFolder(), fileName);

        // 检查文件是否存在，若不存在则创建
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
        // 创建插件的工作目录
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

    // 创建标题配置文件
    private void createTitlesConfig() {
        createFile("titles.yml");
    }

    // 创建主界面配置文件
    private void createHomeConfig() {
        createFile("homes.yml");
    }
}
