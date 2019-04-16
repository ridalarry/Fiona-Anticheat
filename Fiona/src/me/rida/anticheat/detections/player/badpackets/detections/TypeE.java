package me.rida.anticheat.detections.player.badpackets.detections;

import me.rida.anticheat.data.PlayerData;
import me.rida.anticheat.detections.Check;
import me.rida.anticheat.detections.Detection;
import me.rida.anticheat.events.custom.PacketFunkeMoveEvent;

public class TypeE extends Detection {
    public TypeE(Check parentCheck, String id, boolean enabled, boolean executable) {
        super(parentCheck, id, enabled, executable);
    }

    @Override
    public void onFunkeEvent(Object event, PlayerData data) {
        if (event instanceof PacketFunkeMoveEvent) {
            PacketFunkeMoveEvent e = (PacketFunkeMoveEvent) event;

            if (Math.abs(e.getTo().getPitch()) > 90) {
                flag(data, Math.abs(e.getTo().getPitch()) + ">-90", 1, false, true);
            }
        }
    }
}
