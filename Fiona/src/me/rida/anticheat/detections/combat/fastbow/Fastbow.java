package me.rida.anticheat.detections.combat.fastbow;

import me.rida.anticheat.detections.Check;
import me.rida.anticheat.detections.CheckType;
import me.rida.anticheat.detections.combat.fastbow.detections.TypeA;

public class Fastbow
        extends Check {
    public Fastbow() {
        super("Fastbow", CheckType.COMBAT, true, true, false, false, 12, 0);

        addDetection(new TypeA(this, "Type A", true, true));
    }
}

