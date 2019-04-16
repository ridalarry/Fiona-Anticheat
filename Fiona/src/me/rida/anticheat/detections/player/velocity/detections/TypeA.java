package me.rida.anticheat.detections.player.velocity.detections;

import me.rida.anticheat.AntiCheat;
import me.rida.anticheat.data.PlayerData;
import me.rida.anticheat.detections.Check;
import me.rida.anticheat.detections.Detection;
import me.rida.anticheat.events.custom.PacketFunkeMoveEvent;
import me.rida.anticheat.events.custom.PacketRecieveEvent;
import me.rida.anticheat.events.custom.PacketSendEvent;
import me.rida.anticheat.utils.MathUtils;
import com.ngxdev.tinyprotocol.api.Packet;
import com.ngxdev.tinyprotocol.packet.out.WrappedOutVelocityPacket;

public class TypeA extends Detection {
    public TypeA(Check parentCheck, String id, boolean enabled, boolean executable, boolean cancellable) {
        super(parentCheck, id, enabled, executable, cancellable);

        setExperimental(true);
    }

    @Override
    public void onFunkeEvent(Object event, PlayerData data) {
        if (event instanceof PacketFunkeMoveEvent) {
            PacketFunkeMoveEvent e = (PacketFunkeMoveEvent) event;

            if (data.fromOnGround
                    && !data.blocksOnTop
                    && data.movement.deltaY > 0
                    && MathUtils.playerMoved(e.getFrom(), e.getTo())
                    && data.velocityY > 0) {

                double velocityRatio = data.movement.deltaY / data.velocityY;

                if (velocityRatio < 0.74
                        && !data.hasLag()) {
                    if ((data.velVertVerbose += (velocityRatio < 0.7 ? 5 : 1)) > 7 || velocityRatio < 0.4) {
                        data.velVertVerbose = 0;

                        flag(data, MathUtils.round(velocityRatio, 4) + "<-0.74", 1, true, true);
                    }
                } else {
                    data.velVertVerbose = Math.max(0, data.velVertVerbose - 1);
                }

                debug(data, data.velVertVerbose + ": " + data.velocityY + ", " + data.movement.deltaY);
                data.velocityY = 0;
            }
        } else if (event instanceof PacketRecieveEvent) {
            PacketRecieveEvent e = (PacketRecieveEvent) event;

            if (e.getType().equals(Packet.Client.FLYING)) {
                if (data.velocityY > 0 && data.lastVelocityApplied.hasPassed(5)) {
                    if(data.skippedTicks == 0) {
                        flag(data, "0<-0.74", 1, true, true);
                    }
                    data.velocityY = 0;
                }
            }
        } else if (event instanceof PacketSendEvent) {
            PacketSendEvent e = (PacketSendEvent) event;

            if (e.getType().equals(Packet.Server.ENTITY_VELOCITY)) {
                WrappedOutVelocityPacket velocity = new WrappedOutVelocityPacket(e.getPacket(), e.getPlayer());

                if (velocity.getId() == AntiCheat.getInstance().getBlockBoxManager().getBlockBox().getTrackerId(e.getPlayer())
                        && data.player.isOnGround()) {
                    data.velocityY = velocity.getY();
                    data.lastVelocityApplied.reset();
                    debug(data, "Server: " + velocity.getId() + ", " + velocity.getY() + ", " + AntiCheat.getInstance().getBlockBoxManager().getBlockBox().getTrackerId(e.getPlayer()));
                }
            }
        }
    }
}
