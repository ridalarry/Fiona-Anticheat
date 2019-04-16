package me.rida.anticheat.detections.player.badpackets.detections;

import me.rida.anticheat.data.PlayerData;
import me.rida.anticheat.detections.Check;
import me.rida.anticheat.detections.Detection;
import me.rida.anticheat.events.custom.PacketRecieveEvent;
import me.rida.anticheat.events.custom.PacketSendEvent;
import com.ngxdev.tinyprotocol.api.Packet;
import com.ngxdev.tinyprotocol.packet.in.WrappedInAbilitiesPacket;
import org.bukkit.GameMode;

public class TypeH extends Detection {
    public TypeH(Check parentCheck, String id, boolean enabled, boolean executable) {
        super(parentCheck, id, enabled, executable);
    }

    @Override
    public void onFunkeEvent(Object event, PlayerData data) {
        if (event instanceof PacketRecieveEvent) {
            PacketRecieveEvent e = (PacketRecieveEvent) event;

            if (e.getType().equals(Packet.Client.ABILITIES)) {
                if(data.lastLogin.hasPassed(5)) {
                    WrappedInAbilitiesPacket packet = new WrappedInAbilitiesPacket(e.getPacket(), e.getPlayer());
                    if (!data.sentServerAbilities && (data.lastIsFlying == packet.isFlying()) && packet.isAllowedFlight()) {
                        flag(data, "Invalid abilities packet", 1, false, true);
                    }
                    data.lastGamemode = data.player.getGameMode();
                    data.lastIsFlying = packet.isFlying();
                    data.sentServerAbilities = false;
                }
            }
        } else if (event instanceof PacketSendEvent) {
            PacketSendEvent e = (PacketSendEvent) event;

            if (e.getType().equals(Packet.Server.ABILITIES)) {
                data.sentServerAbilities = true;
            }
        }
    }
}
