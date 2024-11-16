package com.fandtpa.util;

import org.bukkit.Material;

public enum VeinMineableBlocks {
    ORE("_ORE"),
    LOG("_LOG"),
    LEAVES("_LEAVES"),
    PLANKS("_PLANKS"),
    WOOD("_WOOD"),
    STONE("STONE"); // 添加石头的特殊处理

    private final String suffix;

    VeinMineableBlocks(String suffix) {
        this.suffix = suffix;
    }

    public String getSuffix() {
        return suffix;
    }

    // 检查给定的方块类型是否在可连锁挖掘的方块后缀列表中
    public static boolean isVeinMineable(Material material) {
        String materialName = material.toString();
        // 如果是STONE，直接返回true
        if (material == Material.STONE) {
            return true;
        }
        for (VeinMineableBlocks veinMineable : values()) {
            if (materialName.endsWith(veinMineable.getSuffix())) {
                return true;
            }
        }
        return false;
    }

    // 检查是否达到STONE的最大连锁数量
    public static boolean canMineMoreStone(int currentCount) {
        return currentCount < 64; // 如果当前计数小于64，则允许继续连锁挖掘
    }
}
