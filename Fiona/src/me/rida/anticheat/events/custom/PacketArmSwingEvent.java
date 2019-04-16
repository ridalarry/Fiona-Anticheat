package me.rida.anticheat.events.custom;

import me.rida.anticheat.events.system.Event;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class PacketArmSwingEvent extends Event {
    private Player player;
    private boolean lookingAtBlock;

    public PacketArmSwingEvent(Player player, boolean lookingAtBlock) {
        this.player = player;
        this.lookingAtBlock = lookingAtBlock;
    }
}
