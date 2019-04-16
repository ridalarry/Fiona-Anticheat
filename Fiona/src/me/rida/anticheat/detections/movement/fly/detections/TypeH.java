package me.rida.anticheat.detections.movement.fly.detections;

import me.rida.anticheat.data.PlayerData;
import me.rida.anticheat.detections.Check;
import me.rida.anticheat.detections.Detection;
import me.rida.anticheat.events.custom.PacketFunkeMoveEvent;
import me.rida.anticheat.utils.ReflectionsUtil;
import lombok.val;

public class TypeH extends Detection {
    public TypeH(Check parentCheck, String id, boolean enabled, boolean executable) {
        super(parentCheck, id, enabled, executable);
    }

    @Override
    public void onFunkeEvent(Object e, PlayerData data) {
        if (e instanceof PacketFunkeMoveEvent) {
            PacketFunkeMoveEvent event = (PacketFunkeMoveEvent) e;

            val player = event.getPlayer();

            val from = event.getFrom();
            val to = event.getTo();

            val yChange = to.getY() - from.getY();
            val motionY = ReflectionsUtil.getMotionY(player);

            val motionChange = Math.abs(yChange - motionY);

            if (data.lastLogin.hasPassed(40)
                    || player.isFlying()
                    || data.inLiquid
                    || data.velocityY > 0.0
                    || to.getY() < 0.0) {
                return;
            }

            if (motionChange > 2.0E-14 && motionChange < 0.07D || motionChange > 0.87D) {
                if (data.motionThreshold++ > 3) {
                    flag(data, "Y: " + motionChange, 1, false, true);
                }
            }
        }
    }
}
