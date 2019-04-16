package me.rida.anticheat.detections.combat.killaura.detections;

import me.rida.anticheat.data.PlayerData;
import me.rida.anticheat.detections.Check;
import me.rida.anticheat.detections.Detection;
import me.rida.anticheat.events.custom.PacketFunkeMoveEvent;
import me.rida.anticheat.utils.MathUtils;

public class TypeI extends Detection {
    public TypeI(Check parentCheck, String id, boolean enabled, boolean executable) {
        super(parentCheck, id, enabled, executable);

        addConfigValue("minimumSpeedXZ", 0.24);
        addConfigValue("thresholdMultiplier", 1.5);
        addConfigValue("threshold", 15);
        addConfigValue("resetTime", 600);
        setExperimental(true);
    }

    @Override
    public void onFunkeEvent(Object event, PlayerData data) {
        if (event instanceof PacketFunkeMoveEvent) {
            PacketFunkeMoveEvent e = (PacketFunkeMoveEvent) event;

            if (MathUtils.looked(e.getFrom(), e.getTo())
                    && data.lastAttack.hasNotPassed(3)
                    && data.lastHitEntity != null) {

                float[] rotations = MathUtils.getRotations(data, data.lastHitEntity);

                float value = MathUtils.getDelta(e.getTo().getPitch(), rotations[1]);
                float threshold = (float) data.lastHitEntity.getLocation().toVector().distance(e.getTo().toVector()) + (float) data.movement.deltaXZ * (float) (double) getConfigValues().get("thresholdMultiplier");

                if (value < threshold
                        && data.movement.deltaXZ > (double) getConfigValues().get("minimumSpeedXZ")
                        && data.kaAngleVerbose.flag((int) getConfigValues().get("threshold"), (int) getConfigValues().get("resetTime"))) {
                    flag(data, "t: b [" + MathUtils.round(value, 4) + "<-" + MathUtils.round(threshold, 4) + "]", 1, true, true);
                }

                debug(data, value + ", " + data.kaAngleVerbose.getVerbose());
            }
        }
    }
}
