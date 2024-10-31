package com.fandtpa.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract event class for handling player-specific actions asynchronously.
 */
public abstract class PlayerEvent extends Event {
    public final Player targetPlayer; // 使用 final 修饰确保只读访问

    public PlayerEvent(@NotNull Player targetPlayer, boolean isAsync) {
        super(isAsync);
        this.targetPlayer = targetPlayer;
    }

    public final @NotNull Player getTargetPlayer() {
        return this.targetPlayer;
    }
}
