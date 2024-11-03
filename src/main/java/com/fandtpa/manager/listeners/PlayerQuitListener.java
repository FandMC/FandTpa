package com.fandtpa.manager.listeners;

import com.fandtpa.manager.OtpManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final OtpManager otpManager;

    public PlayerQuitListener(OtpManager otpManager) {
        this.otpManager = otpManager;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        otpManager.saveLogoutLocation(event.getPlayer());
    }
}
