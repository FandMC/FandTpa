package com.fand.manager.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OtpManager {

    private final Map<UUID, Location> logoutLocations = new HashMap<>();

    // 保存玩家的退出位置
    public void saveLogoutLocation(Player player) {
        logoutLocations.put(player.getUniqueId(), player.getLocation());
    }

    // 获取玩家的退出位置
    public Location getLogoutLocation(UUID playerUUID) {
        return logoutLocations.get(playerUUID);
    }

}
