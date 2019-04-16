package me.rida.anticheat.detections.combat.criticals;

import me.rida.anticheat.detections.Check;
import me.rida.anticheat.detections.CheckType;
import me.rida.anticheat.detections.combat.criticals.detections.TypeA;
import me.rida.anticheat.detections.combat.criticals.detections.TypeB;
import me.rida.anticheat.detections.combat.criticals.detections.TypeC;

public class Criticals
        extends Check {


    public Criticals() {
        super("Criticals", CheckType.COMBAT, true, true, false, false, 20, 7);

        addDetection(new TypeA(this, "Type A", true, true));
        addDetection(new TypeB(this, "Type B", true, true));
        addDetection(new TypeC(this, "Type C", true, false));
    }
}

