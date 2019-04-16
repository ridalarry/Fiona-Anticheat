package me.rida.anticheat.detections.player.inventory.detections;

import me.rida.anticheat.data.PlayerData;
import me.rida.anticheat.detections.Check;
import me.rida.anticheat.detections.Detection;
import me.rida.anticheat.events.custom.PacketFunkeMoveEvent;

public class TypeA extends Detection {
    public TypeA(Check parentCheck, String id, boolean enabled, boolean executable) {
        super(parentCheck, id, enabled, executable);
    }

    @Override
    public void onFunkeEvent(Object event, PlayerData data) {
        if (event instanceof PacketFunkeMoveEvent) {
            if (data.lastInvClick.hasNotPassed(5)
                    && data.onGround
                    && data.movement.deltaXZ > 0.2
                    && data.invMoveVerbose.flag(3, 250L)) {
                flag(data, "t: " + data.invMoveVerbose.getVerbose(), 1, true, true);
            }

            debug(data, data.invMoveVerbose.getVerbose() + ": " + data.lastInvClick.getPassed());
        }
    }
}
