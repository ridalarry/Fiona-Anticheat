package me.rida.anticheat.detections.world.hand;

import me.rida.anticheat.detections.Check;
import me.rida.anticheat.detections.CheckType;
import me.rida.anticheat.detections.world.hand.detections.TypeA;
import me.rida.anticheat.detections.world.hand.detections.TypeB;

public class Hand extends Check {
    public Hand() {
        super("Hand", CheckType.WORLD, true, true, false, true, 30, 0);

        //addDetection(new TypeA(this, "TypeA", true, false));
        addDetection(new TypeA(this, "Type A", true, true));
        addDetection(new TypeB(this, "Type B", false, false));
    }


}
