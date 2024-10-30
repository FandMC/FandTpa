package fand.fandtpa.listeners;

import fand.fandtpa.Main;
import fand.fandtpa.util.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.entity.Player;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerChatListener implements Listener {

    private final Main plugin;
    private final Random random = new Random();

    public PlayerChatListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String title = plugin.getTitlesConfig().getString(player.getUniqueId().toString());

        if (title != null) {
            // 如果称号没有 []，自动添加
            if (!title.startsWith("[") && !title.endsWith("]")) {
                title = "[" + title + "]";
            }
        }

        // 使用 ChatColor.translateAlternateColorCodes 将 & 颜色代码转换为 ChatColor
        String message = event.getMessage();
        message = ChatColor.translateAlternateColorCodes('&', message);

        // 处理 <green:red> 这样的渐变颜色标签
        message = handleGradientColors(message);

        // 检测 &k 并生成 Minecraft 风格的乱码效果
        message = handleMagicCode(message);

        // 将称号和聊天格式化
        if (title != null) {
            String coloredTitle = ChatColor.translateAlternateColorCodes('&', title);
            event.setFormat(coloredTitle + " " + player.getName() + ": " + message);
        } else {
            event.setFormat(player.getName() + ": " + message);
        }
    }

    // 处理颜色渐变效果
    private String handleGradientColors(String message) {
        Pattern gradientPattern = Pattern.compile("<([a-zA-Z0-9]+):([a-zA-Z0-9]+)>");
        Matcher matcher = gradientPattern.matcher(message);

        if (matcher.find()) {
            String startColor = matcher.group(1); // 起始颜色
            String endColor = matcher.group(2);   // 结束颜色
            String textToColor = message.substring(matcher.end()); // 要渐变的文本

            // 删除原有的 <green:red> 标记
            message = matcher.replaceFirst("");

            // 执行颜色渐变
            String gradientText = applyGradient(startColor, endColor, textToColor);
            message = gradientText;
        }

        return message;
    }

    // 颜色渐变的实现
    private String applyGradient(String startColor, String endColor, String text) {
        StringBuilder coloredText = new StringBuilder();
        int length = text.length();

        // 将 startColor 和 endColor 转换为 RGB 值
        int[] startRGB = ChatColor.hexToRgb(startColor);
        int[] endRGB = ChatColor.hexToRgb(endColor);

        for (int i = 0; i < length; i++) {
            double ratio = (double) i / (length - 1); // 比例
            int red = (int) (startRGB[0] + ratio * (endRGB[0] - startRGB[0]));
            int green = (int) (startRGB[1] + ratio * (endRGB[1] - startRGB[1]));
            int blue = (int) (startRGB[2] + ratio * (endRGB[2] - startRGB[2]));

            String hexColor = ChatColor.rgbToHex(red, green, blue);
            coloredText.append(ChatColor.of(hexColor)).append(text.charAt(i));
        }

        return coloredText.toString();
    }

    // 处理 &k 并生成 Minecraft 风格的乱码效果
    private String handleMagicCode(String message) {
        // 检测 &k 位置
        if (message.contains("&k")) {
            // 查找 &k 之后的内容
            String[] parts = message.split("&k", 2);
            String beforeK = parts[0];
            String afterK = parts[1];

            // 获取要生成乱码的文本部分
            StringBuilder obfuscatedText = new StringBuilder();
            for (char c : afterK.toCharArray()) {
                if (Character.isWhitespace(c)) {
                    obfuscatedText.append(c);
                } else {
                    obfuscatedText.append(generateObfuscatedChar());
                }
            }

            // 返回替换了乱码的字符串
            return beforeK + obfuscatedText.toString();
        }

        return message;
    }

    // 生成随机乱码字符，用于模拟 Minecraft 中的 &k 效果
    private char generateObfuscatedChar() {
        // 这些字符通常用于显示乱码效果，类似于 Minecraft 的 &k 效果
        String obfuscatedChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()-=_+[]{}|;:',.<>/?`~";
        return obfuscatedChars.charAt(random.nextInt(obfuscatedChars.length()));
    }
}
