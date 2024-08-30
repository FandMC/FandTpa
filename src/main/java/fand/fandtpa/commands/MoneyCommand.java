package fand.fandtpa.commands;

import fand.fandtpa.Main;
import fand.fandtpa.economy.EcoManager;
import fand.fandtpa.util.ChatColor;
import fand.fandtpa.util.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MoneyCommand implements CommandExecutor {

    private final Main plugin;
    private final ConfigManager configManager;

    public MoneyCommand(Main plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("money_only_player")));
            return true;
        }

        Player player = (Player) sender;
        EcoManager ecoManager = plugin.getEcoManager();
        String balance = ecoManager.getBalanceAsString(player.getUniqueId());

        String message = configManager.getMessage("money_balance");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message.replace("{balance}", balance)));
        return true;
    }
}
