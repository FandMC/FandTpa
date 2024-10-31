package com.fand.util;

import org.bukkit.Location;

public class PortalData {
    private final Location corner1;
    private final Location corner2;
    private final String command;
    private String particleEffect;

    public PortalData(Location corner1, Location corner2, String command, String particleEffect) {
        this.corner1 = corner1;
        this.corner2 = corner2;
        this.command = command;
        this.particleEffect = particleEffect;
    }

    public Location getCorner1() {
        return corner1;
    }

    public Location getCorner2() {
        return corner2;
    }

    public String getCommand() {
        return command;
    }

    public String getParticleEffect() {
        return particleEffect;
    }

    public boolean hasParticles() {
        return !particleEffect.equalsIgnoreCase("false");
    }

    public void disableParticles() {
        this.particleEffect = "false";
    }

    public boolean isInside(Location location) {
        return location.getX() >= Math.min(corner1.getX(), corner2.getX()) &&
                location.getX() <= Math.max(corner1.getX(), corner2.getX()) &&
                location.getY() >= Math.min(corner1.getY(), corner2.getY()) &&
                location.getY() <= Math.max(corner1.getY(), corner2.getY()) &&
                location.getZ() >= Math.min(corner1.getZ(), corner2.getZ()) &&
                location.getZ() <= Math.max(corner1.getZ(), corner2.getZ());
    }
}
