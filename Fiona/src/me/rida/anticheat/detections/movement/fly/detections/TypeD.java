package me.rida.anticheat.detections.movement.fly.detections;

import me.rida.anticheat.data.PlayerData;
import me.rida.anticheat.detections.Check;
import me.rida.anticheat.detections.Detection;
import me.rida.anticheat.events.custom.PacketFunkeMoveEvent;
import me.rida.anticheat.utils.BlockUtils;
import me.rida.anticheat.utils.MathUtils;
import me.rida.anticheat.utils.PlayerUtils;
import org.bukkit.potion.PotionEffectType;

public class TypeD extends Detection {

    public TypeD(Check parentCheck, String id, boolean enabled, boolean executable) {
        super(parentCheck, id, enabled, executable);

        addConfigValue("violationToFlag", 7);
        addConfigValue("speedThreshold", 0.13);
        addConfigValue("instantFlagSpeedThreshold", 0.421);
        addConfigValue("jumpBoostMultiplier", 0.11);
        addConfigValue("threshold", 10);
        addConfigValue("resetTime", 2000);
        addConfigValue("verboseToAdd", 2);
        setThreshold((int) getConfigValues().get("violationToFlag"));
    }

    @Override
    public void onFunkeEvent(Object event, PlayerData data) {
        if (event instanceof PacketFunkeMoveEvent) {
            PacketFunkeMoveEvent e = (PacketFunkeMoveEvent) event;

            if (MathUtils.moved(e.getFrom(), e.getTo())
                    && data.onClimbable
                    && !data.player.hasPotionEffect(PotionEffectType.JUMP)
                    && !data.generalCancel) {

                if (data.movement.deltaY > 0 && data.movement.deltaY  > (double) getConfigValues().get("speedThreshold")) {
                    if ((data.ladderVerbose.flagB((int) getConfigValues().get("threshold"), (int) getConfigValues().get("verboseToAdd")) || MathUtils.getVerticalDistance(e.getFrom(), e.getTo()) > getJumpUpwardsMotion(data))) {
                        flag(data, String.valueOf(MathUtils.round(data.movement.deltaY, 4)), 1, false, true);
                    }
                } else {
                    data.ladderVerbose.setVerbose(0);
                }

                debug(data, data.ladderVerbose.getVerbose() + ": " + data.movement.deltaY + " > 0.13");

            }
        }
    }

    private double getJumpUpwardsMotion(PlayerData data) {
        return (double) getConfigValues().get("instantFlagSpeedThreshold")
                + PlayerUtils.getPotionEffectLevel(data.player, PotionEffectType.JUMP) * (double) getConfigValues().get("jumpBoostMultiplier");
    }
}
