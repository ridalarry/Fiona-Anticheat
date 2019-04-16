package me.rida.anticheat.detections.movement.speed.detections;

import me.rida.anticheat.data.PlayerData;
import me.rida.anticheat.detections.Check;
import me.rida.anticheat.detections.Detection;
import me.rida.anticheat.events.custom.PacketFunkeMoveEvent;
import me.rida.anticheat.utils.PlayerUtils;
import org.bukkit.potion.PotionEffectType;

public class TypeD extends Detection {
    public TypeD(Check parentCheck, String id, boolean enabled, boolean executable) {
        super(parentCheck, id, enabled, executable);

        setExperimental(true);
    }

    @Override
    public void onFunkeEvent(Object event, PlayerData data) {
        if (event instanceof PacketFunkeMoveEvent) {
            PacketFunkeMoveEvent e = (PacketFunkeMoveEvent) event;

            float deltaXZ = (float) Math.hypot(e.getTo().getX() - e.getFrom().getX(), e.getTo().getZ() - e.getFrom().getZ()),
                    threshold = data.onGround ? 0.3f : 0.34f;

            threshold += PlayerUtils.getPotionEffectLevel(data.player, PotionEffectType.SPEED) * (data.onGround ? 0.061f : 0.048f);
            threshold += data.halfBlockTicks > 0 ? (data.onGround ? 0.2f : 0.1f) : 0;
            threshold += PlayerUtils.getDepthStriderLevel(data.player) > 1 ? 0.05f : 0;
            threshold += data.lastBlockPlace.hasNotPassed(12) ? 0.02f : 0;
            threshold += (data.player.getWalkSpeed() - 0.2f) * 1.2f;
            threshold *= data.blockTicks > 0 ? 2.2f : 1.0;
            threshold *= data.iceTicks > 0 && data.groundTicks < 5 ? (!data.onGround ? 2.4f : 1.35f) : 1.0f;

            if (deltaXZ > threshold && !data.generalCancel && !data.isVelocityTaken()) {
                if (data.speedTypeDVerbose.flag(50, 1000L)) {
                    flag(data, deltaXZ + ">-" + threshold, 1, false, true);
                }
            } else {
                data.speedTypeDVerbose.deduct();
            }
        }
    }
}
