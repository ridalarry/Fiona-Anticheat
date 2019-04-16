package me.rida.anticheat.events.custom;

import me.rida.anticheat.events.system.Event;
import com.ngxdev.tinyprotocol.packet.in.WrappedInWindowClickPacket;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PacketInvClickEvent extends Event {

    private Player player;
    private WrappedInWindowClickPacket.ClickType action;
    private ItemStack item;

    public PacketInvClickEvent(Player player, WrappedInWindowClickPacket.ClickType action, ItemStack item) {
        this.player = player;
        this.action = action;
        this.item = item;
    }

    public Player getPlayer() {
        return player;
    }

    public WrappedInWindowClickPacket.ClickType getAction() {
        return action;
    }

    public ItemStack getItem() {
        return item;
    }
}
