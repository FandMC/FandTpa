package fand.fandtpa.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GmTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            // 提供游戏模式的补全选项
            return Arrays.asList("0", "1", "2", "3", "survival", "creative", "adventure", "spectator");
        } else if (args.length == 2) {
            // 提供在线玩家的补全选项
            List<String> playerNames = new ArrayList<>();
            Bukkit.getOnlinePlayers().forEach(player -> playerNames.add(player.getName()));
            return playerNames;
        }
        return null; // 返回null意味着不提供任何补全
    }
}
