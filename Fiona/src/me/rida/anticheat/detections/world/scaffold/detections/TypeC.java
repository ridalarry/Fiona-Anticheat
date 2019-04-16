package me.rida.anticheat.detections.world.scaffold.detections;

import me.rida.anticheat.data.PlayerData;
import me.rida.anticheat.detections.Check;
import me.rida.anticheat.detections.Detection;
import me.rida.anticheat.utils.BlockUtils;
import me.rida.anticheat.utils.MathUtils;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class TypeC extends Detection {
    public TypeC(Check parentCheck, String id, boolean enabled, boolean executable) {
        super(parentCheck, id, enabled, executable);
    }

    @Override
    public void onBukkitEvent(Event event, PlayerData data) {
        if (event instanceof PlayerMoveEvent) {
            PlayerMoveEvent e = (PlayerMoveEvent) event;

            data.beforeYaw = e.getPlayer().getEyeLocation().getYaw();
        } else if (event instanceof BlockPlaceEvent) {
            BlockPlaceEvent e = (BlockPlaceEvent) event;

            if (Math.abs(e.getPlayer().getEyeLocation().getYaw() - data.beforeYaw) > 60
                    && data.movement.deltaXZ > 0.2
                    && BlockUtils.isSolid(e.getBlockPlaced())
                    && e.getBlockPlaced().getType().getId() != 60
                    && data.scaffoldSnapVerbose.flag(3, 200L)) {
                flag(data, MathUtils.round(Math.abs(e.getPlayer().getEyeLocation().getYaw() - data.beforeYaw), 4) + ">-60", 1, true, true);
            }
            debug(data, Math.abs(e.getPlayer().getEyeLocation().getYaw() - data.beforeYaw) + "");
        }
    }
}
