package me.rida.anticheat.detections.combat.killaura.detections;

import me.rida.anticheat.data.PlayerData;
import me.rida.anticheat.detections.Check;
import me.rida.anticheat.detections.Detection;
import me.rida.anticheat.events.custom.PacketArmSwingEvent;
import me.rida.anticheat.events.custom.PacketRecieveEvent;
import me.rida.anticheat.utils.Color;
import com.ngxdev.tinyprotocol.api.Packet;

public class TypeB extends Detection {
    public TypeB(Check parentCheck, String id, boolean enabled, boolean executable) {
        super(parentCheck, id, enabled, executable);
    }

    @Override
    public void onFunkeEvent(Object event, PlayerData data) {
        if (event instanceof PacketRecieveEvent) {
            PacketRecieveEvent e = (PacketRecieveEvent) event;

            switch (e.getType()) {
                case Packet.Client.POSITION_LOOK:
                case Packet.Client.LOOK:
                case Packet.Client.LEGACY_LOOK:
                case Packet.Client.LEGACY_POSITION_LOOK:
                    if (data.lastClick.hasNotPassed(6)) {
                        if (data.lastClick.getPassed() == data.lastTwo) {
                            if (data.killauraLVerbose.flag(100, 400L)) {
                                flag(data, "t: " + data.killauraLVerbose.getVerbose(), 1, true, true);
                            }
                            debug(data, Color.Green + "Flagged: " + data.killauraLVerbose.getVerbose());
                        }
                        debug(data, "One: " + data.lastClick.getPassed());
                    }
                    break;
            }
        } else if (event instanceof PacketArmSwingEvent) {
            PacketArmSwingEvent e = (PacketArmSwingEvent) event;

            if(e.isLookingAtBlock()) return;
            debug(data, "Two: " + data.lastClick.getPassed());
            data.lastTwo = (int) data.lastClick.getPassed();
            data.lastClick.reset();
        }
    }
}
