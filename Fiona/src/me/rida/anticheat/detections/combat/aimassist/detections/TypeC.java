package me.rida.anticheat.detections.combat.aimassist.detections;

import me.rida.anticheat.data.PlayerData;
import me.rida.anticheat.detections.Check;
import me.rida.anticheat.detections.Detection;
import me.rida.anticheat.events.custom.PacketFunkeMoveEvent;
import me.rida.anticheat.utils.MathUtils;

public class TypeC extends Detection {
    public TypeC(Check parentCheck, String id, boolean enabled, boolean executable) {
        super(parentCheck, id, enabled, executable);

        addConfigValue("minYawDelta", 1.5);
        addConfigValue("typeA.min", 0.001);
        addConfigValue("typeA.max", 0.08);
        addConfigValue("typeB.min", 0.24);
        addConfigValue("typeB.max", 0.3);
        addConfigValue("typeC.max", 0D);
        addConfigValue("threshold", 40);
        addConfigValue("resetTime", 300);
        addConfigValue("violationsToFlag", 10);
        setThreshold((int) getConfigValues().get("violationsToFlag"));
    }

    @Override
    public void onFunkeEvent(Object event, PlayerData data) {
        if (event instanceof PacketFunkeMoveEvent) {
            PacketFunkeMoveEvent e = (PacketFunkeMoveEvent) event;

            if (MathUtils.looked(e.getFrom(), e.getTo())) {

                float shitDelta = shit(data.pitchDelta);
                if (!data.usingOptifine
                        && data.yawDelta > (double) getConfigValues().get("minYawDelta")
                        && Math.abs(e.getTo().getPitch()) < 80
                        && ((shitDelta > (double) getConfigValues().get("typeA.min") && shitDelta < (double) getConfigValues().get("typeA.max")) || (shitDelta < (double) getConfigValues().get("typeB.max") && shitDelta > (double) getConfigValues().get("typeB.min")) || data.pitchDelta <= (double) getConfigValues().get("typeC.max"))
                        && data.aimPatternPitchVerbose.flag((int) getConfigValues().get("threshold"), (int) getConfigValues().get("resetTime"))) {
                    flag(data, shitDelta + "<-0.04", 1, true, true);
                }
                debug(data, data.aimPatternPitchVerbose.getVerbose() + ": " + shitDelta + " , " + data.yawDelta);
            }
        }
    }

    private float shit(float value) {
        return ((float) Math.cbrt((value / 0.15f) / 8f) - 0.2f) / .6f;
    }
}