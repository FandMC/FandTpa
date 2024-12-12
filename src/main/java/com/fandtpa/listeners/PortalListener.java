package com.fandtpa.listeners;

import com.fandtpa.Main;
import com.fandtpa.util.PortalData;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PortalListener implements Listener {

    private final Main plugin;

    public PortalListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Location to = event.getTo();
        for (PortalData portalData : plugin.getPortalMap().values()) {
            if (portalData.isInside(to)) {
                plugin.executePortalCommand(event.getPlayer(), portalData);
                break;
            }
        }
    }
}
