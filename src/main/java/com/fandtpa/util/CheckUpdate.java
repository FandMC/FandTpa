package com.fandtpa.util;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CheckUpdate {
    private final JavaPlugin plugin;

    public CheckUpdate(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void check(String currentVersion) {
        /*new Thread(() -> {
            try {
                URL url = new URL("https://api.fandmc.cn/fandtpa/update.json");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder responseBuilder = new StringBuilder();
                    String inputLine;

                    while ((inputLine = in.readLine()) != null) {
                        responseBuilder.append(inputLine);
                    }
                    in.close();

                    // 解析JSON
                    JSON json = new JSON(responseBuilder.toString());
                    double latestVersion = json.getDouble("latest");

                    if (latestVersion > Double.parseDouble(currentVersion)) {
                        plugin.getLogger().info("发现新版本: " + latestVersion);
                    } else {
                        plugin.getLogger().info("当前版本是最新的。");
                    }
                } else {
                    plugin.getLogger().severe("无法连接到更新服务器，响应代码: " + connection.getResponseCode());
                }
            } catch (Exception e) {
                plugin.getLogger().severe("检查更新时发生错误: " + e.getMessage());
            }
        }).start();*/
        plugin.getLogger().severe("服务器正在维护,目前为最新版本=?");
    }
}
