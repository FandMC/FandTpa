package com.fandtpa.commands;

import com.fandtpa.Main;
import com.fandtpa.util.ChatColor;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

@SuppressWarnings("ALL")
public class TprandomCommand implements CommandExecutor {

    private final Main plugin;

    public TprandomCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e正在寻找安全的位置，请稍候..."));

            // 添加短暂的失明效果
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 1, false, false));

            new BukkitRunnable() {
                @Override
                public void run() {
                    Location randomLocation = getRandomSafeLocation(player);
                    if (randomLocation != null) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                // 传送玩家到安全位置
                                player.teleport(randomLocation);

                                // 发送成功消息并显示坐标
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                        "&a你已经被随机传送到安全位置！位置: &bX: " + randomLocation.getBlockX()
                                                + " Y: " + randomLocation.getBlockY()
                                                + " Z: " + randomLocation.getBlockZ()));

                                // 在玩家传送后触发白烟效果
                                spawnSmokeEffect(player.getLocation());
                            }
                        }.runTask(plugin);
                    } else {
                        // 如果找不到安全位置，提醒玩家失败
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                        "&c无法找到安全位置进行随机传送，请稍后重试！"));
                            }
                        }.runTask(plugin);
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
        int maxAttempts = 5;

        for (int attempts = 0; attempts < maxAttempts; attempts++) {
            double x = player.getLocation().getX() + random.nextInt(20001) - 10000;
            double z = player.getLocation().getZ() + random.nextInt(20001) - 10000;
            double y = world.getHighestBlockYAt((int) x, (int) z) + 1;
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
        return material.isSolid() && !material.equals(Material.LAVA) && !material.equals(Material.CACTUS)
                && !material.equals(Material.FIRE) && !material.equals(Material.MAGMA_BLOCK);
    }

    private void spawnSmokeEffect(Location location) {
        World world = location.getWorld();
        if (world != null) {
            for (int i = 0; i < 20; i++) {
                double offsetX = (Math.random() - 0.5) * 2.0;
                double offsetY = Math.random();
                double offsetZ = (Math.random() - 0.5) * 2.0;
                world.spawnParticle(Particle.SMOKE, location.clone().add(offsetX, offsetY, offsetZ), 0);
            }
        }
    }
}
