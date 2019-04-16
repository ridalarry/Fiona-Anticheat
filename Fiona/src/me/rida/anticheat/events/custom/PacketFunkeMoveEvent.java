package me.rida.anticheat.events.custom;

import me.rida.anticheat.events.system.Cancellable;
import me.rida.anticheat.events.system.Event;
import me.rida.anticheat.utils.AntiCheatLocation;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class PacketFunkeMoveEvent
        extends Event
        implements Cancellable {
    private Player player;
    private AntiCheatLocation from, to;
    private boolean cancelled, onGround, jumped;

    public PacketFunkeMoveEvent(Player player, AntiCheatLocation from, AntiCheatLocation to, boolean onGround, boolean jumped) {
        this.player = player;
        this.from = from;
        this.to = to;
        this.onGround = onGround;
        this.jumped = jumped;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
