package me.rida.anticheat.detections.combat.aimassist;

import me.rida.anticheat.detections.Check;
import me.rida.anticheat.detections.CheckType;
import me.rida.anticheat.detections.combat.aimassist.detections.*;

import java.util.concurrent.TimeUnit;

public class AimPattern extends Check {

    public AimPattern() {
        super("AimPattern", CheckType.COMBAT, true, true, false, false, 100, TimeUnit.MINUTES.toMillis(5), 50);

        addDetection(new TypeB(this, "Type B", false, false));
        addDetection(new TypeC(this, "Type C", false, false));
        addDetection(new TypeA(this, "Type A", false, false));
        addDetection(new TypeD(this, "Type D", false, false));
    }


}