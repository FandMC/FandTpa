package fand.fandtpa.commands;

import fand.fandtpa.util.ChatColor;
import fand.fandtpa.util.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FlyCommand implements CommandExecutor {

    private final ConfigManager configManager;

    public FlyCommand(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("command_only_player")));
            return true;
        }

        if (!player.hasPermission("fly.use")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("no_permission")));
            return true;
        }

        boolean canFly = player.getAllowFlight();
        player.setAllowFlight(!canFly);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("fly_toggle").replace("{status}", canFly ? configManager.getMessage("fly_disabled") : configManager.getMessage("fly_enabled"))));

        return true;
    }
}
