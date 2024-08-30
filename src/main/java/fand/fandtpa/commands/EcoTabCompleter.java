package fand.fandtpa.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EcoTabCompleter implements TabCompleter {

    private static final List<String> SUB_COMMANDS = Arrays.asList("set", "add", "take", "balance");

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], SUB_COMMANDS, suggestions);
        }
        // 可以进一步添加玩家名和数字补全逻辑
        return suggestions;
    }
}
