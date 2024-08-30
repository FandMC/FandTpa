package fand.fandtpa.listeners;

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

    // 可选：删除玩家的退出位置（比如当管理员传送后）
    public void removeLogoutLocation(UUID playerUUID) {
        logoutLocations.remove(playerUUID);
    }
}
