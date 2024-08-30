package fand.fandtpa.commands;

import fand.fandtpa.Main;
import fand.fandtpa.util.ChatColor;
import fand.fandtpa.util.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

public class HomeCommand implements CommandExecutor {

    private final Main plugin;
    private final ConfigManager configManager;

    // 构造函数，传递 Main 类的实例
    public HomeCommand(Main plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("home_only_player")));
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("home_usage")));
            return true;
        }

        String subCommand = args[0].toLowerCase();
        switch (subCommand) {
            case "set":
                handleSetHome(player, args);
                break;
            case "tp":
                handleTeleportHome(player, args);
                break;
            case "del":
                handleDeleteHome(player, args);
                break;
            case "list":
                handleListHomes(player);
                break;
            default:
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("home_usage")));
                break;
        }

        return true;
    }

    private void handleSetHome(Player player, String[] args) {
        if (args.length != 2) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("home_set_usage")));
            return;
        }

        String homeName = args[1].toLowerCase();
        saveHome(player, homeName, player.getLocation());
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("home_set_success").replace("{home}", homeName)));
    }

    private void handleTeleportHome(Player player, String[] args) {
        if (args.length != 2) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("home_tp_usage")));
            return;
        }

        String homeName = args[1].toLowerCase();
        Location homeLocation = getHome(player, homeName);
        if (homeLocation != null) {
            player.teleport(homeLocation);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("home_tp_success").replace("{home}", homeName)));
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("home_not_exist").replace("{home}", homeName)));
        }
    }

    private void handleDeleteHome(Player player, String[] args) {
        if (args.length != 2) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("home_del_usage")));
            return;
        }

        String homeName = args[1].toLowerCase();
        deleteHome(player, homeName);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("home_del_success").replace("{home}", homeName)));
    }

    private void handleListHomes(Player player) {
        String path = "homes." + player.getUniqueId();
        FileConfiguration homesConfig = plugin.getHomesConfig();
        if (homesConfig.contains(path)) {
            Map<String, Object> homes = Objects.requireNonNull(homesConfig.getConfigurationSection(path)).getValues(false);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("home_list").replace("{homes}", String.join(", ", homes.keySet()))));
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("home_no_homes")));
        }
    }

    private void saveHome(Player player, String homeName, Location location) {
        String path = "homes." + player.getUniqueId() + "." + homeName;
        FileConfiguration homesConfig = plugin.getHomesConfig();
        homesConfig.set(path + ".world", location.getWorld().getName());
        homesConfig.set(path + ".x", location.getX());
        homesConfig.set(path + ".y", location.getY());
        homesConfig.set(path + ".z", location.getZ());
        homesConfig.set(path + ".yaw", location.getYaw());
        homesConfig.set(path + ".pitch", location.getPitch());
        plugin.saveHomesConfig();
    }

    private Location getHome(Player player, String homeName) {
        String path = "homes." + player.getUniqueId() + "." + homeName;
        FileConfiguration homesConfig = plugin.getHomesConfig();
        if (homesConfig.contains(path)) {
            String world = homesConfig.getString(path + ".world");
            double x = homesConfig.getDouble(path + ".x");
            double y = homesConfig.getDouble(path + ".y");
            double z = homesConfig.getDouble(path + ".z");
            float yaw = (float) homesConfig.getDouble(path + ".yaw");
            float pitch = (float) homesConfig.getDouble(path + ".pitch");
            if (world != null) {
                return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
            }
        }
        return null;
    }

    private void deleteHome(Player player, String homeName) {
        String path = "homes." + player.getUniqueId() + "." + homeName;
        FileConfiguration homesConfig = plugin.getHomesConfig();
        homesConfig.set(path, null);
        plugin.saveHomesConfig();
    }
}
