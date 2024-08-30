package fand.fandtpa.commands;

import fand.fandtpa.Main;
import fand.fandtpa.util.ChatColor;
import fand.fandtpa.util.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TpdenyCommand implements CommandExecutor {

    private final TpaCommand tpaCommand;
    private final TpahereCommand tpahereCommand;
    private final ConfigManager configManager;

    public TpdenyCommand(Main plugin, ConfigManager configManager) {
        this.tpaCommand = (TpaCommand) plugin.getCommand("tpa").getExecutor();
        this.tpahereCommand = (TpahereCommand) plugin.getCommand("tpahere").getExecutor();
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("tpdeny_only_player")));
            return true;
        }

        Player player = (Player) sender;

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
