package com.fandtpa.tab;

import com.fandtpa.Main;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TabListUpdater extends BukkitRunnable {

    private final Main plugin;
    private final boolean papiEnabled;
    private final int refreshRate; // 刷新频率（从配置文件获取）

    public TabListUpdater(Main plugin) {
        this.plugin = plugin;
        this.papiEnabled = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;

        // 从配置文件中读取刷新频率，默认为20（1秒），如果未定义time则使用默认值
        this.refreshRate = plugin.tabConfig.getInt("time", 20);
    }

    @Override
    public void run() {
        // 检查配置文件中是否启用了Tab列表更新
        if (!plugin.tabConfig.getBoolean("enabled", true)) {
            // 如果禁用了Tab列表，则为所有在线玩家清除Tab内容
            for (Player player : Bukkit.getOnlinePlayers()) {
                clearTabList(player);
            }
            return; // 跳过进一步的处理
        }

        // 启用情况下，为每个在线玩家更新Tab列表
        for (Player player : Bukkit.getOnlinePlayers()) {
            updateTabList(player);
        }
    }

    // 更新指定玩家的Tab列表
    private void updateTabList(Player player) {
        // 从配置文件获取Tab的头部和尾部信息
        String header = plugin.tabConfig.getString("header", "");
        String footer = plugin.tabConfig.getString("footer", "");

        // 使用占位符替换相应的文本
        header = replacePlaceholders(player, header);
        footer = replacePlaceholders(player, footer);

        // 设置玩家的Tab列表头部和尾部
        player.sendPlayerListHeaderAndFooter(Component.text(header), Component.text(footer));
    }

    // 清除指定玩家的Tab列表内容
    private void clearTabList(Player player) {
        player.sendPlayerListHeaderAndFooter(Component.empty(), Component.empty());
    }

    // 替换占位符内容
    private String replacePlaceholders(Player player, String text) {
        if (papiEnabled) {
            text = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, text);
        }

        return text.replace("&", "§")
                .replace("%player%", player.getName())
                .replace("%online%", String.valueOf(Bukkit.getOnlinePlayers().size()))
                .replace("%maxplayers%", String.valueOf(Bukkit.getMaxPlayers()))
                .replace("%world%", player.getWorld().getName())
                .replace("%x%", String.valueOf(player.getLocation().getBlockX()))
                .replace("%y%", String.valueOf(player.getLocation().getBlockY()))
                .replace("%z%", String.valueOf(player.getLocation().getBlockZ()))
                .replace("%tps%", getFormattedTPS())
                .replace("%ping%", String.valueOf(player.getPing()));
    }

    // 获取格式化的TPS信息
    private String getFormattedTPS() {
        double[] tps = Bukkit.getServer().getTPS();
        return String.format("§a%.2f §a%.2f §a%.2f", tps[0], tps[1], tps[2]);
    }

    public int getRefreshRate() {
        return refreshRate;
    }
}
