package fand.fandtpa.commands;

import fand.fandtpa.Main;
import fand.fandtpa.util.ChatColor;
import fand.fandtpa.util.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class TpaCommand implements CommandExecutor {

    private final Main plugin;
    private final HashMap<UUID, UUID> tpaRequests;
    private final ConfigManager configManager;

    public TpaCommand(Main plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.tpaRequests = new HashMap<>();
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("tpa_only_player")));
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("tpa_usage")));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("tpa_player_not_found")));
            return true;
        }

        if (player.equals(target)) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("tpa_self_request")));
            return true;
        }

        sendTpaRequest(player, target);
        return true;
    }

    private void sendTpaRequest(Player sender, Player target) {
        UUID senderUUID = sender.getUniqueId();
        UUID targetUUID = target.getUniqueId();

        tpaRequests.put(targetUUID, senderUUID);

        target.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("tpa_request_received")
                .replace("{sender}", sender.getName())));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("tpa_request_sent")
                .replace("{target}", target.getName())));
    }

    public UUID getTpaRequest(UUID targetUUID) {
        return tpaRequests.get(targetUUID);
    }

    public UUID removeTpaRequest(UUID targetUUID) {
        return tpaRequests.remove(targetUUID);
    }
}
