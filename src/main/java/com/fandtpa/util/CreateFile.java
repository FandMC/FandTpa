package com.fandtpa.util;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CreateFile {
    private final Logger logger;
    private final File dataFolder;

    public CreateFile(@NotNull Logger logger, @NotNull File dataFolder) {
        this.logger = logger;
        this.dataFolder = dataFolder;
    }

    public @NotNull File getDataFolder() {
        return this.dataFolder;
    }

    public @NotNull Logger getLogger() {
        return this.logger;
    }

    public File createFile(String fileName) {
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
}
