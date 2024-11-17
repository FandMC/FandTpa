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
import java.util.Objects;
import java.util.Set;

public class VeinMineListener implements Listener {

    private final Main plugin;

    public VeinMineListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        // 检查连锁挖矿是否启用
        if (!((VeinMineCommand) Objects.requireNonNull(plugin.getCommand("toggleVeinMine")).getExecutor()).isVeinMiningEnabled()) return;

        Block block = event.getBlock();
        Material material = block.getType();

        // 检查方块是否为可连锁的类型
        if (VeinMineableBlocks.isVeinMineable(material)) {
            Set<Block> blocksToBreak = findVeinBlocks(block, material, plugin.getMaxVeinMineBlocks(), material == Material.STONE);

            // 破坏所有找到的相连方块
            for (Block b : blocksToBreak) {
                b.breakNaturally();
            }
        }
    }

    /**
     * 查找连锁挖掘的方块集合
     * @param startBlock 起始方块
     * @param material 方块类型
     * @param maxBlocks 最大连锁数量
     * @param isSquare 是否限制为正方形
     * @return 要破坏的方块集合
     */
    private Set<Block> findVeinBlocks(Block startBlock, Material material, int maxBlocks, boolean isSquare) {
        Set<Block> result = new HashSet<>();
        exploreVein(startBlock, material, result, maxBlocks, isSquare, 0);
        return result;
    }

    /**
     * 递归探索连锁挖掘的方块
     * @param block 当前方块
     * @param material 方块类型
     * @param result 结果集合
     * @param maxBlocks 最大连锁数量
     * @param isSquare 是否限制为正方形
     * @param depth 当前递归深度
     */
    private void exploreVein(Block block, Material material, Set<Block> result, int maxBlocks, boolean isSquare, int depth) {
        if (result.size() >= maxBlocks || result.contains(block)) {
            return; // 超过限制或已处理
        }

        if (isSquare && depth > 3) { // 如果是正方形限制，深度不能超过3
            return;
        }

        if (block.getType() == material) {
            result.add(block);

            // 遍历相邻方块
            for (Block adjacent : getAdjacentBlocks(block)) {
                exploreVein(adjacent, material, result, maxBlocks, isSquare, depth + 1);
            }
        }
    }

    /**
     * 获取相邻的方块（六方向）
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
