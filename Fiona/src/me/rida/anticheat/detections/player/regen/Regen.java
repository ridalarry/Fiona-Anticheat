package me.rida.anticheat.detections.player.regen;

import me.rida.anticheat.detections.Check;
import me.rida.anticheat.detections.CheckType;
import me.rida.anticheat.detections.player.regen.detections.TypeA;

public class Regen
        extends Check {
    public Regen() {
        super("Regen", CheckType.PLAYER, true, true, false, false, 5, 0);

        addDetection(new TypeA(this, "Type A", true, true));
    }
}

