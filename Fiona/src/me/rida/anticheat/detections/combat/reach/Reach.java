package me.rida.anticheat.detections.combat.reach;

import me.rida.anticheat.detections.Check;
import me.rida.anticheat.detections.CheckType;
import me.rida.anticheat.detections.combat.criticals.detections.TypeC;
import me.rida.anticheat.detections.combat.reach.detections.TypeA;
import me.rida.anticheat.detections.combat.reach.detections.TypeB;

public class Reach
        extends Check {
    public Reach() {
        super("Reach", CheckType.COMBAT, true, false, false, false, 20, 7);

        addDetection(new TypeA(this, "Type A", true, true));
        addDetection(new TypeB(this, "Type B", false, false));
    }
}

