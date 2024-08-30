package fand.fandtpa.tab;

import fand.fandtpa.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TabListUpdater extends BukkitRunnable {

    private final Main plugin;
    private final boolean papiEnabled;

    public TabListUpdater(Main plugin) {
        this.plugin = plugin;
        this.papiEnabled = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
    }

    @Override
    public void run() {
        if (!plugin.tabConfig.getBoolean("enabled", true)) {
            return;
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            updateTabList(player);
        }
    }

    private void updateTabList(Player player) {
        String header = plugin.tabConfig.getString("header", "");
        String footer = plugin.tabConfig.getString("footer", "");

        // 替换变量
        header = replacePlaceholders(player, header);
        footer = replacePlaceholders(player, footer);

        player.setPlayerListHeaderFooter(header, footer);
    }

    private String replacePlaceholders(Player player, String text) {
        // 使用PAPI替换占位符
        if (papiEnabled) {
            text = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, text);
        }

        // 自定义变量替换
        return text.replace("&", "§")
                .replace("%player%", player.getName())
                .replace("%online%", String.valueOf(Bukkit.getOnlinePlayers().size()))
                .replace("%maxplayers%", String.valueOf(Bukkit.getMaxPlayers()))
                .replace("%world%", player.getWorld().getName())
                .replace("%x%", String.valueOf(player.getLocation().getBlockX()))
                .replace("%y%", String.valueOf(player.getLocation().getBlockY()))
                .replace("%z%", String.valueOf(player.getLocation().getBlockZ()))
                .replace("%tps%", getFormattedTPS())
                .replace("%ping%", String.valueOf(player.getPing())); // 替换ping变量
    }

    // 获取并格式化TPS
    private String getFormattedTPS() {
        double[] tps = Bukkit.getServer().getTPS();
        return String.format("§a%.2f §a%.2f §a%.2f", tps[0], tps[1], tps[2]);
    }
}
