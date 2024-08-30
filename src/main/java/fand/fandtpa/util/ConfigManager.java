package fand.fandtpa.util;

import fand.fandtpa.Main;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ConfigManager {

    private final Main plugin;
    private final Logger logger;
    private final Map<String, String> messages = new HashMap<>();
    private String language;

    public ConfigManager(Main plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        loadConfig();
        loadMessages();
    }

    private void loadConfig() {
        plugin.saveDefaultConfig();
        language = plugin.getConfig().getString("language", "zh_CN");
    }

    private void loadMessages() {
        if (language.equalsIgnoreCase("en_us")) {
            loadEnglishMessages();
        } else {
            loadChineseMessages(); // 默认中文
        }
    }

    private void loadEnglishMessages() {
        messages.put("plugin_enabled", "Plugin enabled successfully!");
        messages.put("plugin_disabled", "Plugin has been disabled!");
        messages.put("error_message", "An error occurred: {error}");
        messages.put("plugin_success", "Plugin started successfully!");
        messages.put("command_only_player", "&cThis command can only be used by players!");
        messages.put("teleport_success", "&aYou have been teleported to your last death location.");
        messages.put("no_death_location", "&cNo death location found.");
        messages.put("log_teleport_success", "{player} has been teleported to their death location: {location}");
        messages.put("log_death_location_recorded", "Recorded death location for {player}: {location}");
        messages.put("no_permission", "&cYou do not have permission to use this command!");
        messages.put("fly_toggle", "&aFly mode is now {status}!");
        messages.put("fly_enabled", "enabled");
        messages.put("fly_disabled", "disabled");
        messages.put("gm_specify_mode", "Please specify a game mode: 0, 1, 2, 3, survival, creative, adventure, spectator");
        messages.put("gm_invalid_mode", "Invalid game mode!");
        messages.put("gm_console_no_self_target", "Console cannot target itself; please specify a player!");
        messages.put("gm_mode_changed", "Game mode changed to: {mode}");
        messages.put("gm_target_mode_changed", "{target}'s game mode changed to: {mode}");
        messages.put("gm_survival", "Survival Mode");
        messages.put("gm_creative", "Creative Mode");
        messages.put("gm_adventure", "Adventure Mode");
        messages.put("gm_spectator", "Spectator Mode");
        messages.put("hat_only_player", "&cOnly players can use this command!");
        messages.put("hat_no_item_in_hand", "&cYou don't have any item in your hand!");
        messages.put("hat_success", "&aYou are now wearing the item in your hand!");
        messages.put("home_only_player", "&cOnly players can use this command!");
        messages.put("home_usage", "&eUsage: /home <set/tp/del/list>");
        messages.put("home_set_usage", "&eUsage: /home set <home_name>");
        messages.put("home_tp_usage", "&eUsage: /home tp <home_name>");
        messages.put("home_del_usage", "&eUsage: /home del <home_name>");
        messages.put("home_set_success", "&aHome &b{home} &ahas been set!");
        messages.put("home_tp_success", "&aTeleported to home &b{home}!");
        messages.put("home_del_success", "&aHome &b{home} &ahas been deleted!");
        messages.put("home_not_exist", "&cHome &b{home} &cdoes not exist!");
        messages.put("home_list", "&aYour homes: &b{homes}");
        messages.put("home_no_homes", "&cYou have not set any homes.");
        messages.put("invsee_only_player", "&cOnly players can use this command!");
        messages.put("invsee_usage", "&eUsage: /invsee <player>");
        messages.put("invsee_viewing", "&aViewing inventory of &b{target}&a.");
        messages.put("invsee_player_not_found", "&cPlayer not found or not online!");
        messages.put("invsee_inventory_suffix", "'s Inventory");
        messages.put("settitle_no_permission", "&cYou do not have permission to use this command!");
        messages.put("settitle_usage", "&eUsage: /settitle <player> <title>");
        messages.put("settitle_player_not_found", "&cPlayer not found or not online!");
        messages.put("settitle_success", "&aTitle {title} has been added to {player}.");
        messages.put("settitle_received", "&aYou have received the title: {title}");
        messages.put("suicide_success", "&cYou have committed suicide!");
        messages.put("suicide_only_player", "&cOnly players can use this command!");
        messages.put("tab_reload_success", "&aTab configuration reloaded successfully.");
        messages.put("tab_reload_usage", "&cUsage: /tab reload");
        messages.put("tpaccept_only_player", "&cThis command can only be used by players!");
        messages.put("tpaccept_request_accepted", "&aTeleport request accepted.");
        messages.put("tpaccept_request_accepted_by_target", "&aYour teleport request has been accepted.");
        messages.put("tpaccept_player_not_found", "&cThe player who made the request is not online or does not exist.");
        messages.put("tpaccept_no_request", "&cYou have no pending teleport requests.");
        messages.put("tpa_only_player", "&cThis command can only be used by players!");
        messages.put("tpa_usage", "&cUsage: /tpa <player>");
        messages.put("tpa_player_not_found", "&cThe player is not online or does not exist.");
        messages.put("tpa_self_request", "&cYou cannot send a teleport request to yourself!");
        messages.put("tpa_request_received", "&a{sender} has requested to teleport to you. Use /tpaccept to accept, or /tpdeny to deny.");
        messages.put("tpa_request_sent", "&aRequest sent to {target}.");
        messages.put("tpahere_only_player", "&cThis command can only be used by players!");
        messages.put("tpahere_usage", "&cUsage: /tpahere <player>");
        messages.put("tpahere_player_not_found", "&cThe player is not online or does not exist.");
        messages.put("tpahere_self_request", "&cYou cannot request a player to teleport to yourself!");
        messages.put("tpahere_request_received", "&a{sender} has requested you to teleport to them. Use /tpaccept to accept, or /tpdeny to deny.");
        messages.put("tpahere_request_sent", "&aRequest sent to {target}.");
        messages.put("tpdeny_only_player", "&cThis command can only be used by players!");
        messages.put("tpdeny_request_denied", "&aTeleport request denied.");
        messages.put("tpdeny_requester_notified", "&cYour teleport request was denied by {player}.");
        messages.put("tpdeny_player_not_found", "&cThe player who sent the request is not online or does not exist.");
        messages.put("tpdeny_no_request", "&cYou do not have any pending teleport requests.");
        messages.put("speed_only_player", "&cThis command can only be used by players.");
        messages.put("speed_usage", "&cUsage: /speed <fly|walk> <speed>");
        messages.put("speed_invalid_number", "&cInvalid speed number.");
        messages.put("speed_invalid_range", "&cSpeed must be between 0.1 and 1.0.");
        messages.put("speed_walk_set", "&aWalk speed set to {speed}.");
        messages.put("speed_fly_set", "&aFly speed set to {speed}.");
        messages.put("speed_fly_enabled", "enabled");
        messages.put("speed_fly_disabled", "disabled");
        messages.put("money_balance", "&aYour current balance is: &e{balance}");
        messages.put("money_only_player", "&cThis command can only be used by players!");
        messages.put("eco_usage", "&eUsage: /eco <set&b|&eadd&b|&etake&b|&ebalance> <player> <amount>");
        messages.put("player_not_found", "&cPlayer not found: {player}");
        messages.put("invalid_amount", "&cPlease enter a valid amount.");
        messages.put("eco_set", "&aSet {player}'s balance to {amount}");
        messages.put("eco_add", "&aAdded {amount} to {player}'s balance");
        messages.put("eco_take", "&aRemoved {amount} from {player}'s balance");
        messages.put("eco_balance", "&a{player}'s balance: {balance}");
        messages.put("unknown_subcommand", "&cUnknown subcommand: {command}");
        messages.put("otp_usage", "&cUsage: /otp <player>");
        messages.put("otp_player_only", "&cThis command can only be used by players!");
        messages.put("otp_no_permission", "&cYou do not have permission to use this command.");
        messages.put("otp_player_online", "&cPlayer {player} is currently online.");
        messages.put("otp_location_not_found", "&cCould not find the logout location for player {player}.");
        messages.put("otp_teleport_success", "&aYou have been teleported to {player}'s logout location.");
    }

    private void loadChineseMessages() {
        messages.put("plugin_enabled", "插件启用成功！");
        messages.put("plugin_disabled", "插件已禁用");
        messages.put("error_message", "发生错误: {error}");
        messages.put("plugin_success", "插件启动成功！");
        messages.put("command_only_player", "&c该命令只能由玩家使用！");
        messages.put("teleport_success", "&a你已传送到上一次死亡地点");
        messages.put("no_death_location", "&c没有找到死亡地点");
        messages.put("log_teleport_success", "{player} 已传送到死亡地点: {location}");
        messages.put("log_death_location_recorded", "记录 {player} 的死亡地点: {location}");
        messages.put("no_permission", "&c你没有权限使用这个命令！");
        messages.put("fly_toggle", "&a飞行模式已{status}！");
        messages.put("fly_enabled", "开启");
        messages.put("fly_disabled", "关闭");
        messages.put("gm_specify_mode", "请指定游戏模式：0, 1, 2, 3, survival, creative, adventure, spectator");
        messages.put("gm_invalid_mode", "无效的游戏模式！");
        messages.put("gm_console_no_self_target", "控制台不能指定自己为目标，请指定一个玩家！");
        messages.put("gm_mode_changed", "游戏模式已更改为: {mode}");
        messages.put("gm_target_mode_changed", "{target} 的游戏模式已更改为: {mode}");
        messages.put("gm_survival", "生存模式");
        messages.put("gm_creative", "创造模式");
        messages.put("gm_adventure", "冒险模式");
        messages.put("gm_spectator", "旁观者模式");
        messages.put("hat_only_player", "&c只有玩家可以使用此命令！");
        messages.put("hat_no_item_in_hand", "&c你手中没有物品！");
        messages.put("hat_success", "&a你已经戴上了手中的物品！");
        messages.put("home_only_player", "&c该命令只能由玩家使用！");
        messages.put("home_usage", "&e使用方法: /home <set/tp/del/list>");
        messages.put("home_set_usage", "&e使用方法: /home set <家名>");
        messages.put("home_tp_usage", "&e使用方法: /home tp <家名>");
        messages.put("home_del_usage", "&e使用方法: /home del <家名>");
        messages.put("home_set_success", "&a家 &b{home} &a已设置");
        messages.put("home_tp_success", "&a已传送到家 &b{home}!");
        messages.put("home_del_success", "&a家 &b{home} &a已删除");
        messages.put("home_not_exist", "&c家 &b{home} &c不存在！");
        messages.put("home_list", "&a你的家: &b{homes}");
        messages.put("home_no_homes", "&c你没有设置任何家。");
        messages.put("invsee_only_player", "&c只有玩家可以使用此命令！");
        messages.put("invsee_usage", "&e用法: /invsee <玩家名>");
        messages.put("invsee_viewing", "&a正在查看 &b{target}&a 的背包。");
        messages.put("invsee_player_not_found", "&c玩家不在线或不存在！");
        messages.put("invsee_inventory_suffix", " 的背包");
        messages.put("settitle_no_permission", "&c你没有权限使用此命令！");
        messages.put("settitle_usage", "&e使用方法: /settitle <玩家> <称号>");
        messages.put("settitle_player_not_found", "&c玩家不在线或不存在！");
        messages.put("settitle_success", "&a称号 {title} 已添加给 {player}.");
        messages.put("settitle_received", "&a你已获得称号: {title}");
        messages.put("suicide_success", "&c你已经自杀了！");
        messages.put("suicide_only_player", "&c只有玩家可以使用这个命令！");
        messages.put("tab_reload_success", "&aTab 配置文件重新加载成功。");
        messages.put("tab_reload_usage", "&c用法: /tab reload");
        messages.put("tpaccept_only_player", "&c该命令只能由玩家使用！");
        messages.put("tpaccept_request_accepted", "&a传送请求已接受。");
        messages.put("tpaccept_request_accepted_by_target", "&a你的传送请求已被接受。");
        messages.put("tpaccept_player_not_found", "&c请求的玩家不在线或不存在。");
        messages.put("tpaccept_no_request", "&c你没有任何传送请求。");
        messages.put("tpa_only_player", "&c该命令只能由玩家使用！");
        messages.put("tpa_usage", "&c使用方法: /tpa <玩家名>");
        messages.put("tpa_player_not_found", "&c玩家不在线或不存在。");
        messages.put("tpa_self_request", "&c你不能请求传送到自己！");
        messages.put("tpa_request_received", "&a{sender} 请求传送到你这里。使用 /tpaccept 接受，/tpdeny 拒绝。");
        messages.put("tpa_request_sent", "&a请求已发送给 {target}。");
        messages.put("tpahere_only_player", "&c该命令只能由玩家使用！");
        messages.put("tpahere_usage", "&c使用方法: /tpahere <玩家名>");
        messages.put("tpahere_player_not_found", "&c玩家不在线或不存在。");
        messages.put("tpahere_self_request", "&c你不能请求玩家传送到你自己！");
        messages.put("tpahere_request_received", "&a{sender} 请求你传送到他那里。使用 /tpaccept 接受，/tpdeny 拒绝。");
        messages.put("tpahere_request_sent", "&a请求已发送给 {target}。");
        messages.put("tpdeny_only_player", "&c该命令只能由玩家使用！");
        messages.put("tpdeny_request_denied", "&a传送请求已拒绝。");
        messages.put("tpdeny_requester_notified", "&c你的传送请求被 {player} 拒绝。");
        messages.put("tpdeny_player_not_found", "&c请求的玩家不在线或不存在。");
        messages.put("tpdeny_no_request", "&c你没有任何传送请求。");
        messages.put("speed_only_player", "&c只有玩家可以使用这个命令。");
        messages.put("speed_usage", "&c用法: /speed <fly|walk> <speed>");
        messages.put("speed_invalid_number", "&c速度参数无效。");
        messages.put("speed_invalid_range", "&c速度必须在0.1和1.0之间。");
        messages.put("speed_walk_set", "&a行走速度已设置为 {speed}。");
        messages.put("speed_fly_set", "&a飞行速度已设置为 {speed}。");
        messages.put("speed_fly_enabled", "已启用");
        messages.put("speed_fly_disabled", "已禁用");
        messages.put("money_only_player", "&c该命令只能由玩家使用！");
        messages.put("money_balance", "&a你当前的金钱为: &e{balance}");
        messages.put("eco_usage", "&e用法: /eco <set&b|&eadd&b|&etake&b|&ebalance> <玩家> <金额>");
        messages.put("player_not_found", "&c未找到玩家: {player}");
        messages.put("invalid_amount", "&c请输入有效的金额。");
        messages.put("eco_set", "&a已将 {player} 的余额设置为 {amount}");
        messages.put("eco_add", "&a已增加 {player} 的余额 {amount}");
        messages.put("eco_take", "&a已减少 {player} 的余额 {amount}");
        messages.put("eco_balance", "&a{player} 的余额: {balance}");
        messages.put("unknown_subcommand", "&c未知的子命令: {command}");
        messages.put("otp_usage", "&c使用方法: /otp <玩家名字>");
        messages.put("otp_player_only", "&c这个命令只能由玩家使用。");
        messages.put("otp_no_permission", "&c你没有权限使用这个命令。");
        messages.put("otp_player_online", "&c玩家 {player} 当前在线。");
        messages.put("otp_location_not_found", "&c无法找到玩家 {player} 的退出位置。");
        messages.put("otp_teleport_success", "&a你已传送到玩家 {player} 的退出位置。");
    }

    public String getMessage(String key) {
        return messages.getOrDefault(key, "§c[Missing language key: " + key + "]");
    }
}
