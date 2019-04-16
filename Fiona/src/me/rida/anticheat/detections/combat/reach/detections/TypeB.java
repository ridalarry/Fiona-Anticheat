package me.rida.anticheat.detections.combat.reach.detections;

import me.rida.anticheat.data.PlayerData;
import me.rida.anticheat.detections.Check;
import me.rida.anticheat.detections.Detection;
import me.rida.anticheat.events.custom.PacketAttackEvent;
import me.rida.anticheat.utils.BoundingBox;
import me.rida.anticheat.utils.MiscUtils;
import me.rida.anticheat.utils.PlayerUtils;
import org.bukkit.GameMode;

public class TypeB extends Detection {
    public TypeB(Check parentCheck, String id, boolean enabled, boolean executable) {
        super(parentCheck, id, enabled, executable);

        addConfigValue("pingAccounting", 0.001);
        addConfigValue("boxExpand", 3.1);

        setExperimental(true);
    }

    @Override
    public void onFunkeEvent(Object event, PlayerData data) {
        if (event instanceof PacketAttackEvent) {
            PacketAttackEvent e = (PacketAttackEvent) event;

            if (data.player.getGameMode() == GameMode.CREATIVE) {
                return;
            }

            BoundingBox entityBox = MiscUtils.getEntityBoundingBox(e.getAttacked());

            double accounting = (double) getConfigValues().get("pingAccounting"), boxExpand = (double) getConfigValues().get("boxExpand");
            float pingAccount = (float) (data.ping * accounting);
            entityBox = entityBox.grow((float) boxExpand + pingAccount, (float) boxExpand + pingAccount, (float) boxExpand + pingAccount);

            entityBox.grow(PlayerUtils.facingOpposite(data.player, e.getAttacked()) ? 0.25f : 0, PlayerUtils.facingOpposite(data.player, e.getAttacked()) ? 0.25f : 0, PlayerUtils.facingOpposite(data.player, e.getAttacked()) ? 0.25f : 0);

            if (e.getAttacked().getMaximumNoDamageTicks() > 16 && data.lastReachAttack.hasPassed(e.getAttacked().getMaximumNoDamageTicks()) && !data.boundingBox.intersectsWithBox(entityBox)) {
                if (data.reachTypeBVerbose.flag(2) || !data.boundingBox.intersectsWithBox(entityBox)) {
                    flag(data, entityBox.getMaximum().distance(data.boundingBox.getMaximum()) + ">-3", 1, true, true);
                }
            }

            debug(data, e.getAttacked().getMaximumNoDamageTicks() + ", " + data.lastReachAttack.getPassed() + ", " + entityBox.getMaximum().lengthSquared());
            data.lastReachAttack.reset();
        }
    }
}
