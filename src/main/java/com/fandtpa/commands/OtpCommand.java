package com.fandtpa.commands;

import com.fandtpa.manager.OtpManager;
import com.fandtpa.util.ChatColor;
import com.fandtpa.manager.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class OtpCommand implements CommandExecutor {

    private final OtpManager otpManager;
    private final ConfigManager configManager;

    public OtpCommand(OtpManager otpManager, ConfigManager configManager) {
        this.otpManager = otpManager;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("otp_usage")));
            return false;
        }

        if (!(sender instanceof Player admin)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("player_only")));
            return true;
        }

        if (!admin.hasPermission("otp.use")) {
            admin.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("otp_no_permission")));
            return true;
        }

        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer != null) {
            admin.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("otp_player_online").replace("{player}", args[0])));
            return true;
        }

        UUID targetUUID = Bukkit.getOfflinePlayer(args[0]).getUniqueId();
        Location logoutLocation = otpManager.getLogoutLocation(targetUUID);

        if (logoutLocation == null) {
            admin.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("otp_location_not_found").replace("{player}", args[0])));
        } else {
            admin.teleport(logoutLocation);
            admin.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("otp_teleport_success").replace("{player}", args[0])));
        }

        return true;
    }
}
