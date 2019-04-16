package me.rida.anticheat.detections.combat.criticals.detections;

import me.rida.anticheat.data.PlayerData;
import me.rida.anticheat.detections.Check;
import me.rida.anticheat.detections.Detection;
import me.rida.anticheat.events.custom.PacketAttackEvent;

public class TypeC extends Detection {
    public TypeC(Check parentCheck, String id, boolean enabled, boolean executable) {
        super(parentCheck, id, enabled, executable);


        addConfigValue("minDeltaY", 0D);
        addConfigValue("maxDeltaY", 1E-3D);
        setExperimental(true);
    }

    @Override
    public void onFunkeEvent(Object event, PlayerData data) {
        if (event instanceof PacketAttackEvent) {
            float deltaY = (float) Math.abs(data.movement.deltaY);

            if (deltaY > (double) getConfigValues().get("minDeltaY") && deltaY < (double) getConfigValues().get("maxDeltaY")) {
                if (data.criticalsCVerbose.flag(10, 450L)) {
                    flag(data, (double) getConfigValues().get("maxDeltaY")+ "-<" + deltaY + ">-0", 1, true, true);
                }
            }
            debug(data, deltaY + "");
        }
    }
}
