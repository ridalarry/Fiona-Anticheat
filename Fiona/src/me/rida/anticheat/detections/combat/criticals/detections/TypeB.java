package me.rida.anticheat.detections.combat.criticals.detections;

import me.rida.anticheat.data.PlayerData;
import me.rida.anticheat.detections.Check;
import me.rida.anticheat.detections.Detection;
import me.rida.anticheat.events.custom.PacketAttackEvent;
import me.rida.anticheat.utils.MathUtils;

public class TypeB extends Detection {

    public TypeB(Check parentCheck, String id, boolean enabled, boolean executable) {
        super(parentCheck, id, enabled, executable);


        addConfigValue("threshold", 10);
        addConfigValue("resetTime", 1000);
        addConfigValue("verboseToAdd", 2);
        addConfigValue("minFallDistance", 0);
        addConfigValue("minGroundTicks", 10);
        addConfigValue("ticksSinceLastVelocity", 7);
        setThreshold(3);
    }

    @Override
    public void onFunkeEvent(Object event, PlayerData data) {
        if (event instanceof PacketAttackEvent) {
            PacketAttackEvent e = (PacketAttackEvent) event;

            if (!data.generalCancel
                    && data.blockTicks == 0
                    && !data.onHalfBlock
                    && !data.inLiquid
                    && data.groundTicks > (int) getConfigValues().get("minGroundTicks")
                    && e.getAttacker().getFallDistance() > (int) getConfigValues().get("minFallDistance")
                    && data.lastVelocity.hasPassed((int) getConfigValues().get("ticksSinceLastVelocity"))
                    && !data.onSlimeBefore) {
                if (data.criticalsFallVerbose.flag((int) getConfigValues().get("threshold"), (int) getConfigValues().get("resetTime"), (int) getConfigValues().get("verboseToAdd"))) {
                    flag(data, MathUtils.round(e.getAttacker().getFallDistance(), 3) + ">-" + (int) getConfigValues().get("minFallDistance"), 1, true, true);
                }
            } else {
                data.criticalsFallVerbose.deduct();
            }
        }
    }
}
