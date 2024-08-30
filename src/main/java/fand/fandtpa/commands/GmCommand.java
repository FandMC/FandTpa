package fand.fandtpa.commands;

import fand.fandtpa.util.ChatColor;
import fand.fandtpa.util.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GmCommand implements CommandExecutor {

    private final ConfigManager configManager;

    public GmCommand(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("gm_specify_mode")));
            return true;
        }

        String mode = args[0].toLowerCase();
        GameMode gameMode = null;
        String modeName = "";

        switch (mode) {
            case "0":
            case "survival":
                gameMode = GameMode.SURVIVAL;
                modeName = configManager.getMessage("gm_survival");
                break;
            case "1":
            case "creative":
                gameMode = GameMode.CREATIVE;
                modeName = configManager.getMessage("gm_creative");
                break;
            case "2":
            case "adventure":
                gameMode = GameMode.ADVENTURE;
                modeName = configManager.getMessage("gm_adventure");
                break;
            case "3":
            case "spectator":
                gameMode = GameMode.SPECTATOR;
                modeName = configManager.getMessage("gm_spectator");
                break;
            default:
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("gm_invalid_mode")));
                return true;
        }

        List<Player> targets = new ArrayList<>();
        if (args.length > 1) {
            for (Entity entity : Bukkit.selectEntities(sender, args[1])) {
                if (entity instanceof Player) {
                    targets.add((Player) entity);
                }
            }
        } else if (sender instanceof Player) {
            targets.add((Player) sender);
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("gm_console_no_self_target")));
            return true;
        }

        for (Player target : targets) {
            target.setGameMode(gameMode);
            target.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("gm_mode_changed").replace("{mode}", ChatColor.GOLD + modeName + ChatColor.RESET)));
            if (!target.equals(sender)) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("gm_target_mode_changed")
                        .replace("{target}", ChatColor.YELLOW + target.getName() + ChatColor.RESET)
                        .replace("{mode}", ChatColor.GOLD + modeName + ChatColor.RESET)));
            }
        }

        return true;
    }
}
