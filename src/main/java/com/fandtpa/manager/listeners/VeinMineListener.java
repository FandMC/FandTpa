package com.fandtpa.manager.listeners;

import com.fandtpa.Main;
import com.fandtpa.commands.VeinMineCommand;
import com.fandtpa.util.VeinMineableBlocks;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.HashSet;
import java.util.Set;

public class VeinMineListener implements Listener {

    private final Main plugin;

    public VeinMineListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        // 检查连锁挖矿是否启用
        if (!((VeinMineCommand) plugin.getCommand("toggleVeinMine").getExecutor()).isVeinMiningEnabled()) return;

        Block block = event.getBlock();
        Material material = block.getType();

        // 检查方块是否为可连锁的类型
        if (VeinMineableBlocks.isVeinMineable(material)) {
            Set<Block> blocksToBreak = new HashSet<>();
            findConnectedBlocks(block, material, blocksToBreak);

            // 破坏所有找到的相连方块
            for (Block b : blocksToBreak) {
                b.breakNaturally();
            }
        }
    }

    /**
     * 递归查找相连的同类型方块
     * @param block 当前方块
     * @param material 方块类型
     * @param blocksToBreak 要破坏的方块集合
     */
    private void findConnectedBlocks(Block block, Material material, Set<Block> blocksToBreak) {
        // 达到最大连锁数量或已包含此方块则停止
        if (blocksToBreak.size() >= plugin.getMaxVeinMineBlocks() || blocksToBreak.contains(block)) {
            return;
        }

        blocksToBreak.add(block);

        // 遍历相邻方块
        for (Block adjacent : getAdjacentBlocks(block)) {
            if (adjacent.getType() == material) {
                findConnectedBlocks(adjacent, material, blocksToBreak);
            }
        }
    }

    /**
     * 获取相邻的六个方块
     * @param block 当前方块
     * @return 相邻方块的集合
     */
    private Set<Block> getAdjacentBlocks(Block block) {
        Set<Block> adjacentBlocks = new HashSet<>();
        adjacentBlocks.add(block.getRelative(1, 0, 0));
        adjacentBlocks.add(block.getRelative(-1, 0, 0));
        adjacentBlocks.add(block.getRelative(0, 1, 0));
        adjacentBlocks.add(block.getRelative(0, -1, 0));
        adjacentBlocks.add(block.getRelative(0, 0, 1));
        adjacentBlocks.add(block.getRelative(0, 0, -1));
        return adjacentBlocks;
    }

    /**
     * 静态方法用于注销监听器
     */
    public static void unregister(Listener listener) {
        HandlerList.unregisterAll(listener);
    }
}
