package com.fandtpa.commands.command;

import com.fandtpa.util.ChatColor;
import com.fandtpa.util.ConfigManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

public class BackCommand implements CommandExecutor, Listener {

    private final HashMap<UUID, Location> deathLocations = new HashMap<>();
    private final Logger logger;
    private final ConfigManager configManager;

    public BackCommand(Logger logger, ConfigManager configManager) {
        this.logger = logger;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("command_only_player")));
            return true;
        }

        UUID playerUUID = player.getUniqueId();
        Location deathLocation = deathLocations.get(playerUUID);

        if (deathLocation != null) {
            player.teleport(deathLocation);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("teleport_success")));
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("no_death_location")));
        }

        return true;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        UUID playerUUID = player.getUniqueId();
        Location deathLocation = player.getLocation();
        deathLocations.put(playerUUID, deathLocation);
        logger.info(configManager.getMessage("log_death_location_recorded")
                .replace("{player}", player.getName())
                .replace("{location}", deathLocation.toString()));
    }
}
