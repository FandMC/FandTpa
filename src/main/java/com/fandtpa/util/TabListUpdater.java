package com.fandtpa.util;

import com.fandtpa.Main;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TabListUpdater extends BukkitRunnable {

    private final Main plugin;
    private final boolean papiEnabled;
    private final int refreshRate; // 刷新频率（从配置文件获取）

    public TabListUpdater(Main plugin) {
        this.plugin = plugin;
        this.papiEnabled = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;

        // 从配置文件中读取刷新频率，默认为20（1秒），如果未定义time则使用默认值
        this.refreshRate = plugin.tabConfig.getInt("time", 20);
    }

    @Override
    public void run() {
        // 检查配置文件中是否启用了Tab列表更新
        if (!plugin.tabConfig.getBoolean("enabled", true)) {
            // 如果禁用了Tab列表，则为所有在线玩家清除Tab内容
            for (Player player : Bukkit.getOnlinePlayers()) {
                clearTabList(player);
            }
            return; // 跳过进一步的处理
        }

        // 启用情况下，为每个在线玩家更新Tab列表
        for (Player player : Bukkit.getOnlinePlayers()) {
            updateTabList(player);
        }
    }

    // 更新指定玩家的Tab列表
    private void updateTabList(Player player) {
        // 从配置文件获取Tab的头部和尾部信息
        String header = plugin.tabConfig.getString("header", "");
        String footer = plugin.tabConfig.getString("footer", "");

        // 使用占位符替换相应的文本
        header = replacePlaceholders(player, header);
        footer = replacePlaceholders(player, footer);

        // 设置玩家的Tab列表头部和尾部
        player.sendPlayerListHeaderAndFooter(Component.text(header), Component.text(footer));
    }

    // 清除指定玩家的Tab列表内容
    private void clearTabList(Player player) {
        player.sendPlayerListHeaderAndFooter(Component.empty(), Component.empty());
    }

    private String replacePlaceholders(Player player, String text) {
        if (papiEnabled) {
            text = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, text);
        }

        // 替换基本占位符
        text = text.replace("&", "§")
                .replace("%player%", player.getName())
                .replace("%online%", String.valueOf(Bukkit.getOnlinePlayers().size()))
                .replace("%maxplayers%", String.valueOf(Bukkit.getMaxPlayers()))
                .replace("%world%", player.getWorld().getName())
                .replace("%x%", String.valueOf(player.getLocation().getBlockX()))
                .replace("%y%", String.valueOf(player.getLocation().getBlockY()))
                .replace("%z%", String.valueOf(player.getLocation().getBlockZ()))
                .replace("%tps%", getFormattedTPS3())
                .replace("%tps3%", getFormattedTPS3())
                .replace("%tps2%", getFormattedTPS2())
                .replace("%tps1%", getFormattedTPS1())
                .replace("%ping%", String.valueOf(player.getPing()))
                .replace("%mspt%", getFormattedMSPT())
                .replace("%health%", String.valueOf(player.getHealth()))
                .replace("%hunger%", String.valueOf(player.getFoodLevel()));

        // 解析条件语句
        text = parseConditions(player, text);

        return text;
    }


    private String parseConditions(Player player, String text) {
        // 匹配条件语句的格式，例如: (if %money% >= "1" {echo:"&a%money%"} else {echo:"暂无"})
        String regex = "\\(if (.+?)\\)"; // 匹配条件结构
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            String condition = matcher.group(1);
            String result = evaluateCondition(player, condition); // 解析并计算条件
            text = text.replace(matcher.group(0), result);
        }

        return text;
    }


    private String evaluateCondition(Player player, String condition) {
        // 提取条件格式: %money% >= "1" {echo:"&a%money%"} else {echo:"暂无"}
        String[] parts = condition.split("else", 2); // 按 "else" 分割
        String[] ifParts = parts[0].split("\\{", 2); // 按 "{" 分割条件和结果

        String conditionExpression = ifParts[0].trim(); // 条件表达式，例如: %money% >= "1"
        String trueResult = ifParts.length > 1 ? ifParts[1].replace("}", "").trim() : ""; // 满足条件的结果
        trueResult = extractEcho(trueResult); // 提取 "echo" 内容

        String falseResult = ""; // 默认无 else
        if (parts.length > 1) {
            String[] elseParts = parts[1].split("\\{", 2); // 解析 else 部分
            falseResult = elseParts.length > 1 ? elseParts[1].replace("}", "").trim() : "";
            falseResult = extractEcho(falseResult); // 提取 "echo" 内容
        }

        // 计算条件表达式的值
        boolean conditionMet = evaluateExpression(player, conditionExpression);

        return conditionMet ? trueResult : falseResult;
    }
    private String extractEcho(String text) {
        if (text.startsWith("echo:\"") && text.endsWith("\"")) {
            return text.substring(6, text.length() - 1); // 提取 echo:"..." 中的内容
        }
        return text; // 如果没有 echo，返回原文本
    }


    private boolean evaluateExpression(Player player, String expression) {
        expression = replacePlaceholders(player, expression); // 替换占位符

        // 支持的条件运算符
        if (expression.contains(">=")) {
            String[] parts = expression.split(">=");
            return Double.parseDouble(parts[0].trim()) >= Double.parseDouble(parts[1].trim());
        } else if (expression.contains("<=")) {
            String[] parts = expression.split("<=");
            return Double.parseDouble(parts[0].trim()) <= Double.parseDouble(parts[1].trim());
        } else if (expression.contains(">")) {
            String[] parts = expression.split(">");
            return Double.parseDouble(parts[0].trim()) > Double.parseDouble(parts[1].trim());
        } else if (expression.contains("<")) {
            String[] parts = expression.split("<");
            return Double.parseDouble(parts[0].trim()) < Double.parseDouble(parts[1].trim());
        } else if (expression.contains("==")) {
            String[] parts = expression.split("==");
            return parts[0].trim().equals(parts[1].trim());
        } else if (expression.contains("!=")) {
            String[] parts = expression.split("!=");
            return !parts[0].trim().equals(parts[1].trim());
        }

        return false; // 无法解析条件
    }



    // 获取格式化的TPS信息
    private String getFormattedTPS3() {
        double[] tps = Bukkit.getServer().getTPS();
        return String.format("%s %s %s", formatTPS(tps[0]), formatTPS(tps[1]), formatTPS(tps[2]));
    }

    private String getFormattedTPS2() {
        double[] tps = Bukkit.getServer().getTPS();
        return String.format("%s %s", formatTPS(tps[0]), formatTPS(tps[1]));
    }

    private String getFormattedTPS1() {
        double[] tps = Bukkit.getServer().getTPS();
        return formatTPS(tps[0]);
    }

    private String formatTPS(double tps) {
        if (tps > 20.01) {
            return String.format("§a%.2f*", tps); // 标记为绿色且加 * 号
        } else if (tps >= 18) {
            return String.format("§a%.2f", tps); // 正常绿色
        } else if (tps >= 15) {
            return String.format("§e%.2f", tps); // 黄色警告
        } else {
            return String.format("§c%.2f", tps); // 红色警告
        }
    }
    // 获取格式化的MSPT
    private String getFormattedMSPT() {
        double mspt = calculateMSPT(Bukkit.getServer().getTPS()[0]);
        return formatMSPT(mspt);
    }

    // 计算每 tick 的平均处理时间 (MSPT)
    private double calculateMSPT(double tps) {
        return Math.min(50.0, 1000.0 / tps); // 每秒1000毫秒，20 TPS = 50 MSPT
    }

    // 格式化MSPT
    private String formatMSPT(double mspt) {
        if (mspt <= 50) {
            return String.format("§a%.2fms", mspt); // 绿色表示良好
        } else if (mspt <= 100) {
            return String.format("§e%.2fms", mspt); // 黄色表示警告
        } else {
            return String.format("§c%.2fms", mspt); // 红色表示严重问题
        }
    }
    public int getRefreshRate() {
        return refreshRate;
    }
}
