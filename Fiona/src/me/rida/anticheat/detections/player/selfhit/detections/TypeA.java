package me.rida.anticheat.detections.player.selfhit.detections;

import me.rida.anticheat.data.PlayerData;
import me.rida.anticheat.detections.Check;
import me.rida.anticheat.detections.Detection;
import me.rida.anticheat.events.custom.PacketRecieveEvent;
import com.ngxdev.tinyprotocol.api.Packet;
import com.ngxdev.tinyprotocol.packet.in.WrappedInFlyingPacket;

public class TypeA extends Detection {
    public TypeA(Check parentCheck, String id, boolean enabled, boolean executable) {
        super(parentCheck, id, enabled, executable);
    }

    @Override
    public void onFunkeEvent(Object event, PlayerData data) {
        if (event instanceof PacketRecieveEvent) {
            PacketRecieveEvent e = (PacketRecieveEvent) event;

            if (e.getType().equals(Packet.Client.POSITION)
                    || e.getType().equals(Packet.Client.POSITION_LOOK)) {
                WrappedInFlyingPacket packet = new WrappedInFlyingPacket(e.getPacket(), e.getPlayer());

               /* if(!data.lastSuffocation.needed(2)
                        && !data.inLiquid
                        && data.typeAVerbose.flag(4, 150L)) {
                    flag(data,  + (data.lastSuffocation.max - data.lastSuffocation.ticksLeft) + "<-2", 1, true);
                }*/
            }
        }
    }
}
