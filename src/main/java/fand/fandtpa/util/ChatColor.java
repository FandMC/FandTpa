package fand.fandtpa.util;

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
    ORANGE('g', "§6", "orange"), // 注意：Minecraft 没有直接的橙色代码，使用黄金色代替
    PINK('h', "§d", "pink"),    // 使用浅紫色作为粉色
    CYAN('i', "§b", "cyan"),
    LIME('j', "§a", "lime"),

    // 特殊效果
    BOLD('k', "§l", "bold"),
    ITALIC('l', "§o", "italic"),
    UNDERLINE('m', "§n", "underline"),
    STRIKETHROUGH('n', "§m", "strikethrough"),
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
                    i++; // 跳过下一个字符，因为它是颜色代码的一部分
                    continue;
                }
            }
            sb.append(b[i]);
        }

        return sb.toString();
    }

    public static ChatColor getByChar(char code) {
        for (ChatColor color : values()) {
            if (color.code == code) {
                return color;
            }
        }
        return null;
    }
}
