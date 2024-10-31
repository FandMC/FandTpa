package fand.fandtpa.commands.tab;

import fand.fandtpa.Main;
import fand.fandtpa.util.ChatColor;
import fand.fandtpa.util.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FandTpaCommand implements CommandExecutor, TabCompleter {

    private final Main plugin;
    private final ConfigManager configManager;

    public FandTpaCommand(Main plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            // 默认显示命令列表
            sendHelpMessage(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                if (!sender.hasPermission("fandtpa.reload")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("no_permission")));
                    return true;
                }
                plugin.reloadConfig();
                configManager.reloadMessages();
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("fandtpa_reload_success")));
                break;

            case "help":
                // 显示帮助信息
                sendHelpMessage(sender);
                break;

            default:
                // 未知命令，显示帮助信息
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("unknown_subcommand").replace("{command}", args[0])));
                sendHelpMessage(sender);
                break;
        }
        return true;
    }

    private void sendHelpMessage(CommandSender sender) {
        // 构建命令列表信息
        StringBuilder availableCommands = new StringBuilder(ChatColor.translateAlternateColorCodes('&', "&e可用命令列表:\n"));

        availableCommands.append(ChatColor.translateAlternateColorCodes('&', "&6/tpa &7- 请求传送到某个玩家 &e用法: /tpa <玩家名>\n"));
        availableCommands.append(ChatColor.translateAlternateColorCodes('&', "&6/tpaccept &7- 接受传送请求 &e用法: /tpaccept\n"));
        availableCommands.append(ChatColor.translateAlternateColorCodes('&', "&6/tpdeny &7- 拒绝传送请求 &e用法: /tpdeny\n"));
        availableCommands.append(ChatColor.translateAlternateColorCodes('&', "&6/tpahere &7- 请求其他玩家传送到你这里 &e用法: /tpahere <玩家名>\n"));
        availableCommands.append(ChatColor.translateAlternateColorCodes('&', "&6/home &7- 家的管理命令 &e用法: /home <set/tp/del/list> <家名>\n"));
        availableCommands.append(ChatColor.translateAlternateColorCodes('&', "&6/back &7- 传送到上一次死亡地点 &e用法: /back\n"));
        availableCommands.append(ChatColor.translateAlternateColorCodes('&', "&6/settitle &7- 为玩家设置称号 &e用法: /settitle <玩家> <称号>\n"));
        availableCommands.append(ChatColor.translateAlternateColorCodes('&', "&6/suicide &7- 自杀\n"));
        availableCommands.append(ChatColor.translateAlternateColorCodes('&', "&6/invsee &7- 查看其他玩家的背包 &e用法: /invsee <玩家名>\n"));
        availableCommands.append(ChatColor.translateAlternateColorCodes('&', "&6/hat &7- 将手中的物品戴在头上 &e用法: /hat\n"));
        availableCommands.append(ChatColor.translateAlternateColorCodes('&', "&6/rtp &7- 随机传送 &e用法: /rtp\n"));
        availableCommands.append(ChatColor.translateAlternateColorCodes('&', "&6/fly &7- 切换飞行模式 &e用法: /fly\n"));
        availableCommands.append(ChatColor.translateAlternateColorCodes('&', "&6/gm &7- 快速切换游戏模式 &e用法: /gm <模式>\n"));
        availableCommands.append(ChatColor.translateAlternateColorCodes('&', "&6/tab reload &7- 重新加载TAB配置 &e用法: /tab reload\n"));
        availableCommands.append(ChatColor.translateAlternateColorCodes('&', "&6/speed &7- 设置玩家的速度 &e用法: /speed <fly|walk> <speed>\n"));
        availableCommands.append(ChatColor.translateAlternateColorCodes('&', "&6/eco &7- 管理玩家经济 &e用法: /eco <set|add|take> <玩家> <金额>\n"));
        availableCommands.append(ChatColor.translateAlternateColorCodes('&', "&6/money &7- 获取玩家当前的金钱 &e用法: /money\n"));
        availableCommands.append(ChatColor.translateAlternateColorCodes('&', "&6/otp &7- 将玩家传送到上一次退出的位置 &e用法: /otp\n"));
        availableCommands.append(ChatColor.translateAlternateColorCodes('&', "&6/fandtpa &7- FandTPA插件的主命令 &e用法: /fandtpa <reload|help>\n"));
        availableCommands.append(ChatColor.translateAlternateColorCodes('&', "&6/v &7- 隐身 &e用法: /v\n"));
        availableCommands.append(ChatColor.translateAlternateColorCodes('&', "&6/hd reload &7- 重载全息字 &e用法: /hd reload\n"));

        // 发送命令列表信息
        sender.sendMessage(availableCommands.toString());
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            if ("reload".startsWith(args[0].toLowerCase())) {
                completions.add("reload");
            }
            if ("help".startsWith(args[0].toLowerCase())) {
                completions.add("help");
            }
        }
        return completions;
    }
}
