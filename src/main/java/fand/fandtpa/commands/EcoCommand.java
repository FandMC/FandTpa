package fand.fandtpa.commands;

import fand.fandtpa.Main;
import fand.fandtpa.economy.EcoManager;
import fand.fandtpa.util.ChatColor;
import fand.fandtpa.util.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public class EcoCommand implements CommandExecutor {

    private final Main plugin;
    private final ConfigManager configManager;

    public EcoCommand(Main plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("eco_usage")));
            return true; // 修改为 true，避免命令执行结束后再显示用法提示
        }

        String subCommand = args[0];
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("player_not_found").replace("{player}", args[1])));
            return true;
        }

        EcoManager ecoManager = plugin.getEcoManager();
        BigDecimal amount = BigDecimal.ZERO;

        // 处理子命令balance，不需要金额参数
        if (!subCommand.equalsIgnoreCase("balance")) {
            if (args.length < 3) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("eco_usage")));
                return true;
            }

            try {
                amount = new BigDecimal(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("invalid_amount")));
                return true;
            }
        }

        switch (subCommand.toLowerCase()) {
            case "set":
                ecoManager.setBalance(target.getUniqueId(), amount);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("eco_set")
                        .replace("{player}", target.getName())
                        .replace("{amount}", amount.toPlainString())));
                break;
            case "add":
                ecoManager.addBalance(target.getUniqueId(), amount);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("eco_add")
                        .replace("{player}", target.getName())
                        .replace("{amount}", amount.toPlainString())));
                break;
            case "take":
                ecoManager.addBalance(target.getUniqueId(), amount.negate());
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("eco_take")
                        .replace("{player}", target.getName())
                        .replace("{amount}", amount.toPlainString())));
                break;
            case "balance":
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("eco_balance")
                        .replace("{player}", target.getName())
                        .replace("{balance}", ecoManager.getBalanceAsString(target.getUniqueId()))));
                break;
            default:
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("unknown_subcommand").replace("{command}", subCommand)));
                return true;
        }
        return true;
    }
}
