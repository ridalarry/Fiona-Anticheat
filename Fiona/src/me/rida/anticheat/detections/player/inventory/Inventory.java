package me.rida.anticheat.detections.player.inventory;

import me.rida.anticheat.detections.Check;
import me.rida.anticheat.detections.CheckType;
import me.rida.anticheat.detections.player.inventory.detections.TypeA;
import me.rida.anticheat.detections.player.inventory.detections.TypeB;

public class Inventory extends Check {
    public Inventory() {
        super("Inventory", CheckType.PLAYER, true, true, false, false, 0, 1);

        addDetection(new TypeB(this, "Type B", true, true));
        addDetection(new TypeA(this, "Type A", true, false));
    }


}
