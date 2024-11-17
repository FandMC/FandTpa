package com.fandtpa.util;

import org.bukkit.Material;

public enum VeinMineableBlocks {
    ORE("_ORE"),
    LOG("_LOG"),
    LEAVES("_LEAVES"),
    PLANKS("_PLANKS"),
    WOOD("_WOOD"),
    STONE("STONE"); // 特殊处理石头

    private final String suffix;

    VeinMineableBlocks(String suffix) {
        this.suffix = suffix;
    }

    public String getSuffix() {
        return suffix;
    }

    /**
     * 检查给定的方块类型是否在可连锁挖掘的方块后缀列表中
     */
    public static boolean isVeinMineable(Material material) {
        if (material == Material.STONE) {
            return true; // 石头始终是可连锁的
        }
        for (VeinMineableBlocks veinMineable : values()) {
            if (!veinMineable.equals(STONE) && material.toString().endsWith(veinMineable.getSuffix())) {
                return true; // 其他类型按后缀匹配
            }
        }
        return false;
    }
}
