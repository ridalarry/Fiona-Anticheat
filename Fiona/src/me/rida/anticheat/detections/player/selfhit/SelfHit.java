package me.rida.anticheat.detections.player.selfhit;

import me.rida.anticheat.detections.Check;
import me.rida.anticheat.detections.CheckType;
import me.rida.anticheat.detections.player.selfhit.detections.TypeB;

public class SelfHit extends Check {

    public SelfHit() {
        super("SelfHit", CheckType.PLAYER, true, false, false, false, 8, 1);

        addDetection(new TypeB(this, "Type B", true, true));
    }
}
