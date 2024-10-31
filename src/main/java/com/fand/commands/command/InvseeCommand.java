package com.fand.commands.command;

import com.fand.util.ChatColor;
import com.fand.util.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class InvseeCommand implements CommandExecutor {

    private final ConfigManager configManager;
    private final Map<Material, String> materialNames;
    private final boolean isHeadless;

    public InvseeCommand(ConfigManager configManager) {
        this.configManager = configManager;
        this.materialNames = loadMaterialNames(); // 加载物品的中文名称
        this.isHeadless = GraphicsEnvironment.isHeadless(); // 检测是否为无头环境
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0 || args.length > 2) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("invsee_usage")));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null || !target.isOnline()) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("invsee_player_not_found")));
            return true;
        }

        if (isHeadless || !(sender instanceof Player)) {
            if (args.length == 1) {
                displayInventory(sender, target);
            } else if (args.length == 2) {
                modifyInventory(sender, target, args[1]);
            }
        } else {
            openCustomInventoryViewForConsole(target);
        }

        return true;
    }

    private void displayInventory(CommandSender sender, Player target) {
        ItemStack[] inventoryContents = target.getInventory().getContents();
        StringBuilder inventoryDisplay = new StringBuilder("玩家 " + target.getName() + " 的背包内容:\n");

        for (int i = 0; i < inventoryContents.length; i++) {
            ItemStack item = inventoryContents[i];
            if (item != null && item.getType() != Material.AIR) {
                String itemName = materialNames.getOrDefault(item.getType(), item.getType().toString());
                inventoryDisplay.append("槽位 ").append(i).append(": ").append(itemName).append(" x").append(item.getAmount()).append("\n");
            } else {
                inventoryDisplay.append("槽位 ").append(i).append(": 空\n");
            }
        }

        sender.sendMessage(inventoryDisplay.toString());
    }

    private void modifyInventory(CommandSender sender, Player target, String slotArg) {
        try {
            int slot = Integer.parseInt(slotArg);
            if (slot < 0 || slot >= target.getInventory().getSize()) {
                sender.sendMessage(ChatColor.RED + "无效的槽位号。");
                return;
            }

            target.getInventory().setItem(slot, null);
            sender.sendMessage(ChatColor.GREEN + "成功清除玩家 " + target.getName() + " 的第 " + slot + " 个槽位的物品。");
            target.updateInventory();
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "槽位号必须是一个数字。");
        }
    }

    private void openCustomInventoryViewForConsole(Player target) {
        JFrame frame = new JFrame("查看玩家背包: " + target.getName());
        frame.setLayout(new GridLayout(6, 9));
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        ItemStack[] inventoryContents = target.getInventory().getContents();

        for (int i = 0; i < inventoryContents.length; i++) {
            JButton button = createInventoryButton(target, i, inventoryContents[i]);
            frame.add(button);
        }

        ItemStack offHandItem = target.getInventory().getItemInOffHand();
        JButton offHandButton = createInventoryButton(target, 40, offHandItem); // 副手槽位
        frame.add(offHandButton);

        ItemStack[] armorContents = target.getInventory().getArmorContents();
        for (int i = 0; i < armorContents.length; i++) {
            JButton button = createInventoryButton(target, 36 + i, armorContents[i]); // 盔甲槽位从36到39
            frame.add(button);
        }

        frame.setVisible(true);
    }

    private JButton createInventoryButton(Player target, int slot, ItemStack item) {
        String itemName = "空";
        if (item != null && item.getType() != Material.AIR) {
            itemName = materialNames.getOrDefault(item.getType(), item.getType().toString());
        }

        JButton button = new JButton(itemName);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    target.getInventory().setItem(slot, null);
                    updateInventoryForPlayer(target);
                    button.setText("空");
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    String itemType = JOptionPane.showInputDialog("输入物品类型（英文）:");
                    if (itemType != null && !itemType.trim().isEmpty()) {
                        try {
                            ItemStack newItem = new ItemStack(Material.valueOf(itemType.toUpperCase()));
                            target.getInventory().setItem(slot, newItem);
                            updateInventoryForPlayer(target);
                            button.setText(materialNames.getOrDefault(newItem.getType(), newItem.getType().toString()));
                        } catch (IllegalArgumentException ex) {
                            JOptionPane.showMessageDialog(null, "无效的物品类型！");
                        }
                    }
                }
            }
        });

        return button;
    }

    private void updateInventoryForPlayer(Player player) {
        player.updateInventory();
    }

    private Map<Material, String> loadMaterialNames() {
        Map<Material, String> names = new HashMap<>();
        names.put(Material.AIR, "空气");
        names.put(Material.STONE, "石头");
        names.put(Material.GRANITE, "花岗岩");
        names.put(Material.POLISHED_GRANITE, "磨制花岗岩");
        names.put(Material.DIORITE, "闪长岩");
        names.put(Material.POLISHED_DIORITE, "磨制闪长岩");
        names.put(Material.ANDESITE, "安山岩");
        names.put(Material.POLISHED_ANDESITE, "磨制安山岩");
        names.put(Material.COBBLED_DEEPSLATE, "深板岩");
        names.put(Material.POLISHED_DEEPSLATE, "磨制深板岩");
        names.put(Material.CALCITE, "方解石");
        names.put(Material.TUFF, "凝灰岩");
        names.put(Material.GRASS_BLOCK, "草方块");
        names.put(Material.DIRT, "泥土");
        names.put(Material.COARSE_DIRT, "砂土");
        names.put(Material.PODZOL, "灰化土");
        names.put(Material.ROOTED_DIRT, "根系泥土");
        names.put(Material.MUD, "泥");
        names.put(Material.CRIMSON_NYLIUM, "绯红菌岩");
        names.put(Material.WARPED_NYLIUM, "诡异菌岩");
        names.put(Material.COBBLESTONE, "圆石");
        names.put(Material.OAK_PLANKS, "橡木板");
        names.put(Material.SPRUCE_PLANKS, "云杉木板");
        names.put(Material.BIRCH_PLANKS, "白桦木板");
        names.put(Material.JUNGLE_PLANKS, "丛林木板");
        names.put(Material.ACACIA_PLANKS, "金合欢木板");
        names.put(Material.CHERRY_PLANKS, "樱桃木板");
        names.put(Material.DARK_OAK_PLANKS, "黑橡木板");
        names.put(Material.MANGROVE_PLANKS, "红树木板");
        names.put(Material.BAMBOO_PLANKS, "竹木板");
        names.put(Material.CRIMSON_PLANKS, "绯红木板");
        names.put(Material.WARPED_PLANKS, "诡异木板");
        names.put(Material.SAND, "沙子");
        names.put(Material.RED_SAND, "红沙");
        names.put(Material.GRAVEL, "沙砾");
        names.put(Material.COAL_ORE, "煤矿石");
        names.put(Material.IRON_ORE, "铁矿石");
        names.put(Material.COPPER_ORE, "铜矿石");
        names.put(Material.GOLD_ORE, "金矿石");
        names.put(Material.DIAMOND_ORE, "钻石矿石");
        names.put(Material.NETHER_GOLD_ORE, "下界金矿石");
        names.put(Material.NETHER_QUARTZ_ORE, "下界石英矿石");
        names.put(Material.ANCIENT_DEBRIS, "远古残骸");
        names.put(Material.COAL_BLOCK, "煤炭块");
        names.put(Material.IRON_BLOCK, "铁块");
        names.put(Material.GOLD_BLOCK, "金块");
        names.put(Material.DIAMOND_BLOCK, "钻石块");
        names.put(Material.NETHERITE_BLOCK, "下界合金块");
        names.put(Material.AMETHYST_BLOCK, "紫水晶块");
        names.put(Material.LAPIS_BLOCK, "青金石块");
        names.put(Material.COPPER_BLOCK, "铜块");
        names.put(Material.REDSTONE_ORE, "红石矿石");
        names.put(Material.DEEPSLATE_REDSTONE_ORE, "深板岩红石矿石");
        names.put(Material.EMERALD_ORE, "绿宝石矿石");
        names.put(Material.DEEPSLATE_EMERALD_ORE, "深板岩绿宝石矿石");
        names.put(Material.IRON_INGOT, "铁锭");
        names.put(Material.GOLD_INGOT, "金锭");
        names.put(Material.COPPER_INGOT, "铜锭");
        names.put(Material.DIAMOND, "钻石");
        names.put(Material.EMERALD, "绿宝石");

        return names;
    }
}
