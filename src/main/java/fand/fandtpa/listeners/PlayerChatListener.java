package fand.fandtpa.listeners;

import fand.fandtpa.Main;
import fand.fandtpa.util.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.entity.Player;

public class PlayerChatListener implements Listener {

    private final Main plugin;

    public PlayerChatListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String title = plugin.getTitlesConfig().getString(player.getUniqueId().toString());
        if (title != null) {
            // 使用 ChatColor.translateAlternateColorCodes 将 & 颜色代码转换为 ChatColor
            String coloredTitle = ChatColor.translateAlternateColorCodes('&', title);
            event.setFormat(coloredTitle + " " + player.getName() + ": " + event.getMessage());
        }
    }
}
