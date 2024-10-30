package fand.fandtpa.event;

import java.util.IllegalFormatException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class AsyncPlayerChatEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private static final Logger LOGGER = Logger.getLogger(AsyncPlayerChatEvent.class.getName());

    private boolean cancel = false;
    private final String message;
    private String format = "<%1$s> %2$s";
    private final Set<Player> recipients;

    public AsyncPlayerChatEvent(boolean async, @NotNull Player who, @NotNull String message, @NotNull Set<Player> recipients) {
        super(who, async);
        this.message = message;
        this.recipients = recipients;
    }

    public @NotNull String getMessage() {
        return message;
    }

    public void setFormat(@NotNull String format) {
        try {
            String.format(format, targetPlayer.getName(), message); // 检查格式的正确性
            this.format = format; // 格式无误时赋值
        } catch (IllegalFormatException e) {
            LOGGER.log(Level.SEVERE, "提供的聊天格式无效: " + format, e);
            throw e; // 抛出异常以便调用者处理
        }
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
}
