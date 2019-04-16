package me.rida.anticheat.detections.combat.killaura.detections;

import me.rida.anticheat.data.PlayerData;
import me.rida.anticheat.detections.Check;
import me.rida.anticheat.detections.Detection;
import me.rida.anticheat.events.custom.PacketAttackEvent;

public class TypeJ extends Detection {
    public TypeJ(Check parentCheck, String id, boolean enabled, boolean executable) {
        super(parentCheck, id, enabled, executable);

        addConfigValue("threshold", 4);
        addConfigValue("resetTime", 500);
        addConfigValue("violationsToFlag", 5);
        setThreshold((int) getConfigValues().get("violationsToFlag"));
    }

    @Override
    public void onFunkeEvent(Object event, PlayerData data) {
        if (event instanceof PacketAttackEvent) {
            PacketAttackEvent e = (PacketAttackEvent) event;

            if (e.getAttacker().isDead() && data.killauraDeadVerbose.flag((int) getConfigValues().get("threshold"), (int) getConfigValues().get("resetTime"))) {
                flag(data, "*", 1, false, true);
            }
        }
    }
}
