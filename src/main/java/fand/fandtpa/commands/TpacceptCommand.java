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

public class TpacceptCommand implements CommandExecutor {

    private final TpaCommand tpaCommand;
    private final TpahereCommand tpahereCommand;
    private final ConfigManager configManager;

    public TpacceptCommand(Main plugin, ConfigManager configManager) {
        this.tpaCommand = (TpaCommand) plugin.getCommand("tpa").getExecutor();
        this.tpahereCommand = (TpahereCommand) plugin.getCommand("tpahere").getExecutor();
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("tpaccept_only_player")));
            return true;
        }

        Player player = (Player) sender;

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
                    requester.teleport(player); // 处理 /tpa 请求
                } else {
                    player.teleport(requester); // 处理 /tpahere 请求
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
