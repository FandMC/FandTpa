package fand.fandtpa.commands;

import fand.fandtpa.util.ChatColor;
import fand.fandtpa.util.ConfigManager;
import org.bukkit.Bukkit;
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

public class InvseeCommand implements CommandExecutor {

    private final ConfigManager configManager;

    public InvseeCommand(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("invsee_usage")));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null || !target.isOnline()) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("invsee_player_not_found")));
            return true;
        }

        if (sender instanceof Player player) {
            if (target.equals(player)) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', configManager.getMessage("invsee_cannot_view_own_inventory")));
                return true;
            }
            openCustomInventoryViewForPlayer(player, target);
        } else {
            openCustomInventoryViewForConsole(target);
        }

        return true;
    }

    private void openCustomInventoryViewForPlayer(Player viewer, Player target) {
        viewer.openInventory(target.getInventory());
    }

    private void openCustomInventoryViewForConsole(Player target) {
        JFrame frame = new JFrame("Inventory of " + target.getName());
        frame.setLayout(new GridLayout(6, 9));
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        ItemStack[] inventoryContents = target.getInventory().getContents();

        for (int i = 0; i < 36; i++) {
            JButton button = createInventoryButton(target, i, inventoryContents[i]);
            frame.add(button);
        }

        ItemStack offHandItem = target.getInventory().getItemInOffHand();
        JButton offHandButton = createInventoryButton(target, 36, offHandItem);
        frame.add(offHandButton);

        ItemStack[] armorContents = target.getInventory().getArmorContents();
        for (int i = 0; i < armorContents.length; i++) {
            JButton button = createInventoryButton(target, 45 + i, armorContents[i]);
            frame.add(button);
        }

        frame.setVisible(true);
    }

    private JButton createInventoryButton(Player target, int slot, ItemStack item) {
        JButton button = new JButton(item != null ? item.getType().toString() : "Empty");

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    // 左键点击删除物品
                    target.getInventory().setItem(slot, null);
                    button.setText("Empty");
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    // 右键点击输入物品类型
                    String itemType = JOptionPane.showInputDialog("Enter item type:");
                    if (itemType != null && !itemType.trim().isEmpty()) {
                        try {
                            ItemStack newItem = new ItemStack(org.bukkit.Material.valueOf(itemType.toUpperCase()));
                            target.getInventory().setItem(slot, newItem);
                            button.setText(newItem.getType().toString());
                        } catch (IllegalArgumentException ex) {
                            JOptionPane.showMessageDialog(null, "Invalid item type!");
                        }
                    }
                }
            }
        });

        return button;
    }
}
