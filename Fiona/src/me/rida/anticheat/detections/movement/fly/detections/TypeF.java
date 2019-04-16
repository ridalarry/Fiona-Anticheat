package me.rida.anticheat.detections.movement.fly.detections;

import me.rida.anticheat.data.PlayerData;
import me.rida.anticheat.detections.Check;
import me.rida.anticheat.detections.Detection;
import me.rida.anticheat.events.custom.PacketFunkeMoveEvent;
import me.rida.anticheat.utils.MathUtils;
import me.rida.anticheat.utils.PlayerUtils;

public class TypeF extends Detection {
    public TypeF(Check parentCheck, String id, boolean enabled, boolean executable) {
        super(parentCheck, id, enabled, executable);

        setExperimental(true);
    }

    //TODO Test for false positives inside all possible vehicles.
    @Override
    public void onFunkeEvent(Object event, PlayerData data) {
        if (event instanceof PacketFunkeMoveEvent) {
            PacketFunkeMoveEvent e = (PacketFunkeMoveEvent) event;

            if (MathUtils.playerMoved(e.getFrom(), e.getTo()) && e.getPlayer().isInsideVehicle() && !e.getPlayer().getAllowFlight()) {

                if (data.movement.deltaY > 0 && MathUtils.getDelta(data.movement.deltaY, data.movement.lastDeltaY) < 1E-5 && !PlayerUtils.isGliding(data.player)) {
                    if (data.flyTypeFVerbose.flag(12, 650L)) {
                        flag(data, MathUtils.getDelta(data.movement.deltaY, data.movement.lastDeltaY) + "< 0.00001", 1, true, true);
                    }
                } else {
                    data.flyTypeFVerbose.deduct();
                }
            }
        }
    }
}
