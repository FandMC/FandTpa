package com.fandtpa.util;

public enum ChatColor {
    // 基础颜色
    BLACK('0', "§0", "black"),
    DARK_BLUE('1', "§1", "dark_blue"),
    DARK_GREEN('2', "§2", "dark_green"),
    DARK_AQUA('3', "§3", "dark_aqua"),
    DARK_RED('4', "§4", "dark_red"),
    DARK_PURPLE('5', "§5", "dark_purple"),
    GOLD('6', "§6", "gold"),
    GRAY('7', "§7", "gray"),
    DARK_GRAY('8', "§8", "dark_gray"),
    BLUE('9', "§9", "blue"),
    GREEN('a', "§a", "green"),
    AQUA('b', "§b", "aqua"),
    RED('c', "§c", "red"),
    LIGHT_PURPLE('d', "§d", "light_purple"),
    YELLOW('e', "§e", "yellow"),
    WHITE('f', "§f", "white"),

    // 自定义颜色
    ORANGE('g', "§6", "orange"),
    PINK('h', "§d", "pink"),
    CYAN('i', "§b", "cyan"),
    LIME('j', "§a", "lime"),

    // 特殊效果
    OBFUSCATED('k', "§k", "obfuscated"), // 乱码效果
    BOLD('l', "§l", "bold"),
    ITALIC('m', "§o", "italic"),
    UNDERLINE('n', "§n", "underline"),
    STRIKETHROUGH('o', "§m", "strikethrough"),
    RESET('r', "§r", "reset");

    private final char code;
    private final String mcCode;
    private final String name;

    // 构造函数
    ChatColor(char code, String mcCode, String name) {
        this.code = code;
        this.mcCode = mcCode;
        this.name = name;
    }

    @Override
    public String toString() {
        return mcCode;
    }

    public String getName() {
        return name;
    }

    public static String translateAlternateColorCodes(char altColorChar, String textToTranslate) {
        StringBuilder sb = new StringBuilder();
        char[] b = textToTranslate.toCharArray();

        for (int i = 0; i < b.length; i++) {
            if (b[i] == altColorChar && i + 1 < b.length) {
                ChatColor color = getByChar(b[i + 1]);
                if (color != null) {
                    sb.append(color.mcCode);
                    i++;
                    continue;
                }
            }
            sb.append(b[i]);
        }

        return sb.toString();
    }

    // 根据字符获取颜色或特殊效果
    public static ChatColor getByChar(char code) {
        for (ChatColor color : values()) {
            if (color.code == code) {
                return color;
            }
        }
        return null;
    }

    // 将十六进制颜色转换为 RGB
    public static int[] hexToRgb(String hex) {
        return new int[]{
                Integer.valueOf(hex.substring(0, 2), 16),
                Integer.valueOf(hex.substring(2, 4), 16),
                Integer.valueOf(hex.substring(4, 6), 16)
        };
    }

    // 将 RGB 转换为十六进制
    public static String rgbToHex(int red, int green, int blue) {
        return String.format("%02x%02x%02x", red, green, blue);
    }

    // 生成自定义颜色代码，支持使用 HEX 颜色
    public static String of(String hexColor) {
        return "§x§" + hexColor.charAt(0) + "§" + hexColor.charAt(1) +
                "§" + hexColor.charAt(2) + "§" + hexColor.charAt(3) +
                "§" + hexColor.charAt(4) + "§" + hexColor.charAt(5);
    }
}
