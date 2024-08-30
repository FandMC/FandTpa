package fand.fandtpa.commands;

import fand.fandtpa.util.ChatColor;
import fand.fandtpa.util.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SuicideCommand implements CommandExecutor {

    private final ConfigManager configManager;

    public SuicideCommand(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            // 强制确保玩家死亡
            forcePlayerDeath(player);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("suicide_success")));
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("suicide_only_player")));
        }
        return true;
    }

    private void forcePlayerDeath(Player player) {
        // 尝试通过设置生命值为0让玩家死亡
        player.setHealth(0);

        // 如果玩家仍未死亡，应用其他方法确保死亡
        if (!player.isDead()) {
            player.damage(1000); // 给予大额伤害确保玩家死亡

            // 仍然未能成功则应用即时伤害药水效果
            if (!player.isDead()) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.HARM, 1, 10)); // 高等级的即时伤害效果

                // 如果仍然有效，应用持续伤害效果
                if (!player.isDead()) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 200, 1)); // 持续的枯萎效果，最终导致死亡
                }
            }
        }
    }
}
