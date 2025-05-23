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

public class TpacceptCommand implements CommandExecutor {

    private final TpaCommand tpaCommand;
    private final TpahereCommand tpahereCommand;
    private final ConfigManager configManager;

    public TpacceptCommand(Main plugin, ConfigManager configManager) {
        this.tpaCommand = (TpaCommand) Objects.requireNonNull(plugin.getCommand("tpa")).getExecutor();
        this.tpahereCommand = (TpahereCommand) Objects.requireNonNull(plugin.getCommand("tpahere")).getExecutor();
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("tpaccept_only_player")));
            return true;
        }

        UUID requesterUUID = tpaCommand.getTpaRequest(player.getUniqueId());
        if (requesterUUID == null) {
            requesterUUID = tpahereCommand.getTpahereRequest(player.getUniqueId());
        }

        if (requesterUUID != null) {
            Player requester = Bukkit.getPlayer(requesterUUID);
            if (requester != null && requester.isOnline()) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("tpaccept_request_accepted")));
                requester.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("tpaccept_request_accepted_by_target")));

                if (tpaCommand.getTpaRequest(player.getUniqueId()) != null) {
                    requester.teleport(player);
                } else {
                    player.teleport(requester);
                }

                tpaCommand.removeTpaRequest(player.getUniqueId());
                tpahereCommand.removeTpahereRequest(player.getUniqueId());
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("tpaccept_player_not_found")));
            }
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("tpaccept_no_request")));
        }

        return true;
    }
}
