package me.rida.anticheat.detections.world.block;

import me.rida.anticheat.detections.Check;
import me.rida.anticheat.detections.CheckType;
import me.rida.anticheat.detections.world.block.detections.FastBreak1_8;
import me.rida.anticheat.detections.world.block.detections.FastBreak1_9;

public class Block extends Check {
    public Block() {
        super("Block", CheckType.WORLD, false, false, false, false, 120, 0);

        addDetection(new FastBreak1_9(this, "FastBreak", true, false));
        addDetection(new FastBreak1_8(this, "FastBreak", true, false));
    }
}
