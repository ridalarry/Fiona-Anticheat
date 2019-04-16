package me.rida.anticheat.detections.movement.jesus;

import me.rida.anticheat.detections.Check;
import me.rida.anticheat.detections.CheckType;
import me.rida.anticheat.detections.movement.jesus.detections.TypeA;
import me.rida.anticheat.detections.movement.jesus.detections.TypeB;

public class Jesus extends Check {

    public Jesus() {
        super("Jesus", CheckType.MOVEMENT, true, true, false, false, 50, 20);

        addDetection(new TypeB(this, "Type B", true, true));
        addDetection(new TypeA(this, "Type A", true, true));
    }


}
