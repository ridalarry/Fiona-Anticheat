package me.rida.anticheat.detections.player.badpackets.detections;

import me.rida.anticheat.data.PlayerData;
import me.rida.anticheat.detections.Check;
import me.rida.anticheat.detections.Detection;
import me.rida.anticheat.events.custom.PacketRecieveEvent;
import me.rida.anticheat.utils.MathUtils;
import com.ngxdev.tinyprotocol.api.Packet;

public class TypeA extends Detection {

    public TypeA(Check parentCheck, String id, boolean enabled, boolean executable) {
        super(parentCheck, id, enabled, executable);
    }

    //TODO Test when home
    @Override
    public void onFunkeEvent(Object event, PlayerData data) {
        if (event instanceof PacketRecieveEvent) {
            PacketRecieveEvent e = (PacketRecieveEvent) event;

            if ((e.getType().contains("Look")
                    || e.getType().contains("Position")
                    || e.getType().equals(Packet.Client.FLYING))
                    && data.lastTeleport.hasPassed(3)) {
                if (data.timerTicks++ >= 30) {
                    long elapsed = MathUtils.elapsed(data.timerStart);

                    if (elapsed < 1498) {
                        if (data.bpAVerbose++ > 1) {
                            flag(data, elapsed + "<-1500", 1, true, true);
                        }
                    } else {
                        data.bpAVerbose = 0;
                    }

                    data.timerTicks = 0;
                    data.timerStart = System.currentTimeMillis();
                }
            }
        }
    }

}
