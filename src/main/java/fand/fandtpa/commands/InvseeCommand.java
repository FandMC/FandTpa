package fand.fandtpa.commands;

import fand.fandtpa.util.ChatColor;
import fand.fandtpa.util.ConfigManager;
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
            // 无头环境或非玩家，使用命令行输出
            if (args.length == 1) {
                displayInventory(sender, target);
            } else if (args.length == 2) {
                modifyInventory(sender, target, args[1]);
            }
        } else {
            // 支持图形化界面，使用GUI
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
            target.updateInventory();  // 同步客户端
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
                    // 左键点击删除物品
                    target.getInventory().setItem(slot, null);
                    updateInventoryForPlayer(target);
                    button.setText("空");
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    // 右键点击输入物品类型
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
        // 强制服务器和客户端同步背包状态
        player.updateInventory();
    }

    private Map<Material, String> loadMaterialNames() {
        Map<Material, String> names = new HashMap<>();
        // 手动填充部分常用物品的中文名称
        names.put(Material.STONE, "石头");
        names.put(Material.DIRT, "泥土");
        // 添加更多物品
        // 你可以手动将所有需要的物品翻译好，或者从现有的翻译文件加载
        return names;
    }
}
