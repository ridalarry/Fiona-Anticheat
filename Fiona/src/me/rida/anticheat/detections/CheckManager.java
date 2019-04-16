package me.rida.anticheat.detections;

import me.rida.anticheat.detections.combat.aimassist.AimPattern;
import me.rida.anticheat.detections.combat.autoclicker.AutoClicker;
import me.rida.anticheat.detections.combat.criticals.Criticals;
import me.rida.anticheat.detections.combat.fastbow.Fastbow;
import me.rida.anticheat.detections.combat.killaura.KillAura;
import me.rida.anticheat.detections.combat.reach.Reach;
import me.rida.anticheat.detections.movement.fly.Fly;
import me.rida.anticheat.detections.movement.invalidmotion.InvalidMotion;
import me.rida.anticheat.detections.movement.jesus.Jesus;
import me.rida.anticheat.detections.movement.nofall.NoFall;
import me.rida.anticheat.detections.movement.noslowdown.NoSlowdown;
import me.rida.anticheat.detections.movement.speed.Speed;
import me.rida.anticheat.detections.movement.sprint.Sprint;
import me.rida.anticheat.detections.movement.step.Step;
import me.rida.anticheat.detections.player.badpackets.BadPackets;
import me.rida.anticheat.detections.player.inventory.Inventory;
import me.rida.anticheat.detections.player.regen.Regen;
import me.rida.anticheat.detections.player.selfhit.SelfHit;
import me.rida.anticheat.detections.player.velocity.Velocity;
import me.rida.anticheat.detections.world.block.Block;
import me.rida.anticheat.detections.world.hand.Hand;
import me.rida.anticheat.detections.world.scaffold.Scaffold;
import me.rida.anticheat.utils.Violation;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class CheckManager {
    private final List<Check> checks;
    public Map<UUID, List<Violation>> violations;

    public CheckManager() {
        checks = new CopyOnWriteArrayList<>();
        violations = new ConcurrentHashMap<>();
        initializeDetections();
    }

    public void initializeDetections() {
        addCheck(new KillAura());
        addCheck(new Reach());
        addCheck(new Speed());
        addCheck(new Fly());
        addCheck(new Step());
        addCheck(new Criticals());
        addCheck(new Fastbow());
        addCheck(new NoFall());
        addCheck(new BadPackets());
        addCheck(new Regen());
        addCheck(new Scaffold());
        addCheck(new NoSlowdown());
        addCheck(new AutoClicker());
        addCheck(new AimPattern());
        addCheck(new Jesus());
        addCheck(new Sprint());
        addCheck(new SelfHit());
        addCheck(new Inventory());
        addCheck(new Hand());
        addCheck(new Velocity());
        addCheck(new Block());
        addCheck(new InvalidMotion());
    }

    public List<Check> getChecks() {
        return checks;
    }

    public Check getCheckByName(String name) {
        for (Check check : checks) {
            if (check.getName().equalsIgnoreCase(name)) {
                return check;
            }
        }
        return null;
    }

    public boolean isCheck(String name) {
        return getChecks().stream().anyMatch(check -> check.getName().equalsIgnoreCase(name));
    }

    public List<Detection> getDetections(Check check) {
        return check.getDetections();
    }

    private void addCheck(Check check) {
        checks.add(check);
    }

    public void removeCheck(Check check) {
        checks.remove(check);
    }

    public void unregisterAll() {
        checks.clear();
    }
}

