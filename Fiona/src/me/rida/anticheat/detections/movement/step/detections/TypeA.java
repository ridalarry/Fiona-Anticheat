package me.rida.anticheat.detections.movement.step.detections;

import me.rida.anticheat.data.PlayerData;
import me.rida.anticheat.detections.Check;
import me.rida.anticheat.detections.Detection;
import me.rida.anticheat.utils.MathUtils;
import me.rida.anticheat.utils.PlayerUtils;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

public class TypeA extends Detection {
    public TypeA(Check parentCheck, String id, boolean enabled, boolean executable) {
        super(parentCheck, id, enabled, executable);

        setExperimental(true);
    }

    @Override
    public void onBukkitEvent(Event event, PlayerData data) {
        if (event instanceof PlayerMoveEvent) {
            PlayerMoveEvent e = (PlayerMoveEvent) event;

            if (!PlayerUtils.isRiskyForFlight(data)
                    && MathUtils.playerMoved(e.getFrom(), e.getTo())
                    && data.movement.deltaY > 0.6 + (PlayerUtils.getPotionEffectLevel(data.player, PotionEffectType.JUMP) * 0.1)) {
                flag(data, data.movement.deltaY + ">-0.6", 1, false, true);
            }
        }
    }
}
