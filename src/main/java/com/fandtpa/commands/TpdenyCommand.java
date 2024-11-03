package com.fandtpa.commands;

import com.fandtpa.Main;
import com.fandtpa.util.ChatColor;
import com.fandtpa.manager.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class TpdenyCommand implements CommandExecutor {

    private final TpaCommand tpaCommand;
    private final TpahereCommand tpahereCommand;
    private final ConfigManager configManager;

    public TpdenyCommand(Main plugin, ConfigManager configManager) {
        this.tpaCommand = (TpaCommand) Objects.requireNonNull(plugin.getCommand("tpa")).getExecutor();
        this.tpahereCommand = (TpahereCommand) Objects.requireNonNull(plugin.getCommand("tpahere")).getExecutor();
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("tpdeny_only_player")));
            return true;
        }

        UUID requesterUUID = tpaCommand.removeTpaRequest(player.getUniqueId());
        if (requesterUUID == null) {
            requesterUUID = tpahereCommand.removeTpahereRequest(player.getUniqueId());
        }

        if (requesterUUID != null) {
            Player requester = Bukkit.getPlayer(requesterUUID);
            if (requester != null && requester.isOnline()) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("tpdeny_request_denied")));
                requester.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("tpdeny_requester_notified")
                        .replace("{player}", player.getName())));
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("tpdeny_player_not_found")));
            }
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("tpdeny_no_request")));
        }

        return true;
    }
}
