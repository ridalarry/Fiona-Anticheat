package me.rida.anticheat.detections.movement.step.detections;

import me.rida.anticheat.data.PlayerData;
import me.rida.anticheat.detections.Check;
import me.rida.anticheat.detections.Detection;
import me.rida.anticheat.events.custom.PacketFunkeMoveEvent;
import me.rida.anticheat.utils.MathUtils;
import me.rida.anticheat.utils.PlayerUtils;

public class TypeB extends Detection {

    public TypeB(Check parentCheck, String id, boolean enabled, boolean executable) {
        super(parentCheck, id, enabled, executable);

        addConfigValue("maxStep", 1.2);
        addConfigValue("airTickThreshold", 20);
        addConfigValue("yDeltaThreshold", 0.2);
    }

    @Override
    public void onFunkeEvent(Object event, PlayerData data) {
        if (event instanceof PacketFunkeMoveEvent) {
            PacketFunkeMoveEvent e = (PacketFunkeMoveEvent) event;

            if (MathUtils.playerMoved(e.getFrom(), e.getTo()) && data.movement.deltaY > 0 && data.halfBlockTicks == 0 && !PlayerUtils.isRiskyForFlight(data)) {
                if (((data.stepTotalYDist += data.movement.deltaY) % 0.5 == 0) && data.stepTotalYDist > 0.51) {
                    flag(data, data.stepTotalYDist % 0.5 + "=0", 1, false, true);
                }
                debug(data, data.stepTotalYDist + ", " + data.collidedHorizontally);
            } else {
                data.stepTotalYDist = 0;
            }
        }
    }

}
