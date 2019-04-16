package me.rida.anticheat.detections.movement.noslowdown;

import me.rida.anticheat.detections.Check;
import me.rida.anticheat.detections.CheckType;
import me.rida.anticheat.detections.movement.noslowdown.detections.TypeA;
import me.rida.anticheat.detections.movement.noslowdown.detections.TypeB;
import me.rida.anticheat.detections.movement.noslowdown.detections.TypeC;

import java.util.concurrent.TimeUnit;

public class NoSlowdown extends Check {
    public NoSlowdown() {
        super("NoSlowdown", CheckType.MOVEMENT, true, true, false, false, 50, TimeUnit.SECONDS.toMillis(180), 30);

        addDetection(new TypeA(this, "Type A", true, true));
        addDetection(new TypeB(this, "Type B", true, true));
        addDetection(new TypeC(this, "Type C", true, true));
    }
}
