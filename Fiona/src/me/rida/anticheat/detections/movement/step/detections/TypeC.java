package me.rida.anticheat.detections.movement.step.detections;

import me.rida.anticheat.data.PlayerData;
import me.rida.anticheat.detections.Check;
import me.rida.anticheat.detections.Detection;
import me.rida.anticheat.events.custom.PacketFunkeMoveEvent;
import me.rida.anticheat.utils.MathUtils;
import me.rida.anticheat.utils.PlayerUtils;

public class TypeC extends Detection {
    public TypeC(Check parentCheck, String id, boolean enabled, boolean executable) {
        super(parentCheck, id, enabled, executable);

        setExperimental(true);
    }

    @Override
    public void onFunkeEvent(Object event, PlayerData data) {
        if (event instanceof PacketFunkeMoveEvent) {
            PacketFunkeMoveEvent e = (PacketFunkeMoveEvent) event;

            if (data.movement.deltaXZ == 0 && data.movement.deltaY > 0 && !PlayerUtils.isRiskyForFlight(data)) {
                if (MathUtils.getDelta(data.collidedYDist += data.movement.deltaY, 1) > 0.2f) {
                    flag(data, data.collidedYDist + ">-1", 1, true, true);
                }
            } else {
                data.collidedYDist = 0;
            }
        }
    }
}
