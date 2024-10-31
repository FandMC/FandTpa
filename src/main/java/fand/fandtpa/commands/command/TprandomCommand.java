package fand.fandtpa.commands.command;

import fand.fandtpa.Main;
import fand.fandtpa.util.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class TprandomCommand implements CommandExecutor {

    private final Main plugin;

    public TprandomCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e正在寻找安全的位置，请稍候..."));
            new BukkitRunnable() {
                @Override
                public void run() {
                    Location randomLocation = getRandomSafeLocation(player);
                    if (randomLocation != null) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                player.teleport(randomLocation);
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                        "&a你已经被随机传送到安全位置！位置: &bX: " + randomLocation.getBlockX()
                                                + " Y: " + randomLocation.getBlockY()
                                                + " Z: " + randomLocation.getBlockZ()));
                            }
                        }.runTask(plugin);
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&c无法找到安全位置进行随机传送，请稍后重试！"));
                    }
                }
            }.runTaskAsynchronously(plugin);
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c只有玩家可以使用此命令！"));
        }
        return true;
    }

    private Location getRandomSafeLocation(Player player) {
        Random random = new Random();
        World world = player.getWorld();
        int maxAttempts = 10;

        for (int attempts = 0; attempts < maxAttempts; attempts++) {
            double x = player.getLocation().getX() + random.nextInt(20001) - 10000;
            double z = player.getLocation().getZ() + random.nextInt(20001) - 10000;
            double y = world.getHighestBlockYAt((int) x, (int) z) + 1; // 确保玩家不会在地下生成
            Location randomLocation = new Location(world, x, y, z);

            if (isSafeLocation(randomLocation)) {
                return randomLocation;
            }
        }
        return null;
    }

    private boolean isSafeLocation(Location location) {
        World world = location.getWorld();
        if (world == null || location.getBlockY() <= 0 || location.getBlockY() >= world.getMaxHeight()) {
            return false;
        }
        Location below = location.clone().subtract(0, 1, 0);
        Material groundMaterial = below.getBlock().getType();
        Material feetMaterial = location.getBlock().getType();
        Material headMaterial = location.clone().add(0, 1, 0).getBlock().getType();

        return isSolidGround(groundMaterial) && feetMaterial.equals(Material.AIR) && headMaterial.equals(Material.AIR);
    }

    private boolean isSolidGround(Material material) {
        return material.isSolid() && !material.equals(Material.LAVA) && !material.equals(Material.CACTUS);
    }
}
