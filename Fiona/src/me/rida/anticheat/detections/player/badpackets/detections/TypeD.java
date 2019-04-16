package me.rida.anticheat.detections.player.badpackets.detections;

import me.rida.anticheat.data.PlayerData;
import me.rida.anticheat.detections.Check;
import me.rida.anticheat.detections.Detection;
import me.rida.anticheat.events.custom.PacketFunkeMoveEvent;
import me.rida.anticheat.utils.MathUtils;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

public class TypeD extends Detection {

    public TypeD(Check parentCheck, String id, boolean enabled, boolean executable) {
        super(parentCheck, id, enabled, executable);

        addConfigValue("maxMove", 20.0);

        addConfigValue("violationToFlag", 40);
        setThreshold((int) getConfigValues().getOrDefault("violationToFlag", 40));
    }

    @Override
    public void onBukkitEvent(Event event, PlayerData data) {
        if (event instanceof PlayerMoveEvent) {
            PlayerMoveEvent e = (PlayerMoveEvent) event;

            double lDelta = e.getFrom().toVector().distance(e.getTo().toVector());
            double threshold = (double) getConfigValues().get("maxMove");
            if (lDelta > threshold) {
                flag(data, MathUtils.round(lDelta, 3) + ">-" + threshold, 11, false, true);
                e.setCancelled(true);
            }
        }
    }
}
