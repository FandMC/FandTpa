package fand.fandtpa.commands;

import fand.fandtpa.util.ChatColor;
import fand.fandtpa.util.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HatCommand implements CommandExecutor {

    private final ConfigManager configManager;

    public HatCommand(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("hat_only_player")));
            return true;
        }

        Player player = (Player) sender;
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if (itemInHand == null || itemInHand.getType().isAir()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("hat_no_item_in_hand")));
            return true;
        }

        // 将当前头盔移到主手，将主手物品放在头盔位置
        ItemStack itemOnHead = player.getInventory().getHelmet();
        player.getInventory().setHelmet(itemInHand);
        player.getInventory().setItemInMainHand(itemOnHead);

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("hat_success")));

        return true;
    }
}
