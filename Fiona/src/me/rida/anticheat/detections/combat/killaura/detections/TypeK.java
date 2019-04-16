package me.rida.anticheat.detections.combat.killaura.detections;

import me.rida.anticheat.data.PlayerData;
import me.rida.anticheat.detections.Check;
import me.rida.anticheat.detections.Detection;
import me.rida.anticheat.events.custom.PacketAttackEvent;

public class TypeK extends Detection {
    public TypeK(Check parentCheck, String id, boolean enabled, boolean executable) {
        super(parentCheck, id, enabled, executable);

        addConfigValue("invInteractTickThreshold", 10);
        addConfigValue("threshold", 5);
        addConfigValue("resetTime", 200);
        addConfigValue("violationsToFlag", 5);
        setThreshold((int) getConfigValues().get("violationsToFlag"));
    }

    @Override
    public void onFunkeEvent(Object event, PlayerData data) {
        if (event instanceof PacketAttackEvent) {
            if (data.lastInvClick.hasNotPassed((int) getConfigValues().get("invInteractTickThreshold"))
                    && data.killauraInvVerbose.flag((int) getConfigValues().get("threshold"), (int) getConfigValues().get("resetTime"))) {
                flag(data, "t: " + data.killauraInvVerbose.getVerbose(), 1, true, true);
            }
        }
    }
}
