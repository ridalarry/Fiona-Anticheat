package me.rida.anticheat.detections.player.velocity;

import me.rida.anticheat.detections.Check;
import me.rida.anticheat.detections.CheckType;
import me.rida.anticheat.detections.player.velocity.detections.TypeA;
import me.rida.anticheat.detections.player.velocity.detections.TypeB;

public class Velocity extends Check {
    public Velocity() {
        super("Velocity", CheckType.PLAYER, true, false, false, false, 4, 1);


        addDetection(new TypeA(this, "Type A", true, false, true));
        addDetection(new TypeB(this, "Type B", false, false));
    }
}
