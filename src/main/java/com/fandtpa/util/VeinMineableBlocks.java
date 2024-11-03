package com.fandtpa.util;

import org.bukkit.Material;

public enum VeinMineableBlocks {
    ORE("_ORE"),
    LOG("_LOG"),
    LEAVES("_LEAVES"),
    PLANKS("_PLANKS"),
    WOOD("_WOOD");

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
        for (VeinMineableBlocks veinMineable : values()) {
            if (materialName.endsWith(veinMineable.getSuffix())) {
                return true;
            }
        }
        return false;
    }
}
