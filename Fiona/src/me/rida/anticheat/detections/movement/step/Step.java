package me.rida.anticheat.detections.movement.step;

import me.rida.anticheat.detections.Check;
import me.rida.anticheat.detections.CheckType;
import me.rida.anticheat.detections.movement.step.detections.TypeA;
import me.rida.anticheat.detections.movement.step.detections.TypeB;
import me.rida.anticheat.detections.movement.step.detections.TypeC;

public class Step extends Check {
    public Step() {
        super("Step", CheckType.MOVEMENT, true, false, false, false, 20, 2);

        addDetection(new TypeB(this, "Type B", true, false));
        addDetection(new TypeA(this, "Type A", false, false));
        //addDetection(new TypeC(this, "Type C", false, false));
    }
}
