package fand.fandtpa.commands.tab;

import fand.fandtpa.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class HomeTabCompleter implements TabCompleter {

    private final Main plugin;

    public HomeTabCompleter(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (!(sender instanceof Player player)) {
            return null;
        }

        UUID playerUUID = player.getUniqueId();
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions = getFirstArgCompletions(args[0]);
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("tp") || args[0].equalsIgnoreCase("del"))) {
            completions = getHomeNameCompletions(playerUUID, args[1]);
        }

        return completions;
    }

    private List<String> getFirstArgCompletions(String input) {
        List<String> subCommands = new ArrayList<>();
        subCommands.add("set");
        subCommands.add("tp");
        subCommands.add("del");
        subCommands.add("list");

        return StringUtil.copyPartialMatches(input, subCommands, new ArrayList<>());
    }

    private List<String> getHomeNameCompletions(UUID playerUUID, String input) {
        List<String> completions = new ArrayList<>();
        String path = "homes." + playerUUID;

        if (plugin.getHomesConfig().contains(path)) {
            Map<String, Object> homes = Objects.requireNonNull(plugin.getHomesConfig().getConfigurationSection(path)).getValues(false);
            completions = StringUtil.copyPartialMatches(input, new ArrayList<>(homes.keySet()), new ArrayList<>());
        }

        return completions;
    }
}
