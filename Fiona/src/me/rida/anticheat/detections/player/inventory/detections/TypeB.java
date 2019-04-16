package me.rida.anticheat.detections.player.inventory.detections;

import me.rida.anticheat.data.PlayerData;
import me.rida.anticheat.detections.Check;
import me.rida.anticheat.detections.Detection;
import me.rida.anticheat.events.custom.PacketInvClickEvent;
import me.rida.anticheat.utils.MathUtils;

public class TypeB extends Detection {
    public TypeB(Check parentCheck, String id, boolean enabled, boolean executable) {
        super(parentCheck, id, enabled, executable);
    }

    @Override
    public void onFunkeEvent(Object event, PlayerData data) {
        if (event instanceof PacketInvClickEvent) {
            PacketInvClickEvent e = (PacketInvClickEvent) event;

            long elapsed = MathUtils.elapsed(data.lastInventoryCheckClick), delta = Math.abs(elapsed - data.lastElapsed);

            if (elapsed > 0
                    && delta <= 20
                    && data.invClickVerbose.flag(10, 500L, 2)) {
                flag(data, delta + "<-10", 1, false, true);
            } else {
                data.invClickVerbose.deduct();
            }

            debug(data, data.invClickVerbose.getVerbose() + ": " + elapsed + ", " + delta + ", " + e.getAction().isCreativeAction() + ", " + e.getAction().isKeyboardClick() + ", " + e.getAction().isLeftClick() + ", " + e.getAction().isRightClick() + ", " + e.getAction().isShiftClick());

            data.lastElapsed = elapsed;
            data.lastInventoryCheckClick = System.currentTimeMillis();
        }
    }
}
