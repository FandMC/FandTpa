package com.fandtpa.commands;

import com.fandtpa.util.ChatColor;
import com.fandtpa.manager.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class TpahereCommand implements CommandExecutor {

    private final HashMap<UUID, UUID> tpahereRequests;
    private final ConfigManager configManager;

    public TpahereCommand(ConfigManager configManager) {
        this.tpahereRequests = new HashMap<>();
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("tpahere_only_player")));
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("tpahere_usage")));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("tpahere_player_not_found")));
            return true;
        }

        if (player.equals(target)) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("tpahere_self_request")));
            return true;
        }

        sendTpahereRequest(player, target);
        return true;
    }

    private void sendTpahereRequest(Player sender, Player target) {
        UUID senderUUID = sender.getUniqueId();
        UUID targetUUID = target.getUniqueId();

        tpahereRequests.put(targetUUID, senderUUID);

        target.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("tpahere_request_received")
                .replace("{sender}", sender.getName())));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("tpahere_request_sent")
                .replace("{target}", target.getName())));
    }

    public UUID getTpahereRequest(UUID targetUUID) {
        return tpahereRequests.get(targetUUID);
    }

    public UUID removeTpahereRequest(UUID targetUUID) {
        return tpahereRequests.remove(targetUUID);
    }
}
