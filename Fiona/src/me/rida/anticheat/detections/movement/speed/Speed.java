package me.rida.anticheat.detections.movement.speed;

import me.rida.anticheat.detections.Check;
import me.rida.anticheat.detections.CheckType;
import me.rida.anticheat.detections.movement.speed.detections.*;

import java.util.concurrent.TimeUnit;

public class Speed extends Check {
    public Speed() {
        super("Speed", CheckType.MOVEMENT, true, true, false, false, 80, TimeUnit.MINUTES.toMillis(4), 15);

        addDetection(new TypeA(this, "Type A", true, true));
        addDetection(new TypeB(this, "Type B", true, false));
        addDetection(new TypeC(this, "Type C", false, false));
        addDetection(new TypeD(this, "Type D", true, true));
    }
}
