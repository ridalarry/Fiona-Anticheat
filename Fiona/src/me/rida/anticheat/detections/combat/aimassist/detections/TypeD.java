package me.rida.anticheat.detections.combat.aimassist.detections;

import me.rida.anticheat.data.PlayerData;
import me.rida.anticheat.detections.Check;
import me.rida.anticheat.detections.Detection;
import me.rida.anticheat.events.custom.PacketFunkeMoveEvent;
import me.rida.anticheat.utils.MathUtils;

public class TypeD extends Detection {
    public TypeD(Check parentCheck, String id, boolean enabled, boolean executable) {
        super(parentCheck, id, enabled, executable);

        setExperimental(true);
    }

    @Override
    public void onFunkeEvent(Object event, PlayerData data) {
        if (event instanceof PacketFunkeMoveEvent) {
            PacketFunkeMoveEvent e = (PacketFunkeMoveEvent) event;

            if (MathUtils.looked(e.getFrom(), e.getTo())) {
                float yaw, yawDif = ((yaw = e.getTo().getYaw()) - e.getFrom().getYaw());

                if (yaw % 0.5 == 0 && !data.isBeingCancelled && data.apIntVerbose.flag(10, 550L)) {
                    flag(data, "t: a; " + yaw, 1, true, true);
                } else if (yawDif % 0.1 == 0 && !data.isBeingCancelled && data.apIntVerbose.flag(20, 450L)) {
                    flag(data, "t: b; " + yawDif, 1, true, true);
                }
            }
        }
    }
}
