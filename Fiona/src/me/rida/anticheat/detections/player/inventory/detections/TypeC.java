package me.rida.anticheat.detections.player.inventory.detections;

import me.rida.anticheat.data.PlayerData;
import me.rida.anticheat.detections.Check;
import me.rida.anticheat.detections.Detection;
import me.rida.anticheat.utils.BlockUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Optional;

public class TypeC extends Detection {
    public TypeC(Check parentCheck, String id, boolean enabled, boolean executable) {
        super(parentCheck, id, enabled, executable);
    }

    @Override
    public void onBukkitEvent(Event event, PlayerData data) {
        if (event instanceof InventoryOpenEvent) {
            InventoryOpenEvent e = (InventoryOpenEvent) event;

            if (!ChatColor.stripColor(e.getInventory().getTitle()).equals("Chest")) {
                return;
            }
            me.rida.anticheat.utils.math.RayTrace trace = new me.rida.anticheat.utils.math.RayTrace(data.player.getEyeLocation().toVector(), data.player.getEyeLocation().getDirection());

            List<Vector> vectors = trace.traverse(4.5, 0.25);

            Optional<Vector> optional = vectors.stream().filter(vec -> BlockUtils.isSolid(new Location(e.getPlayer().getWorld(), vec.getX(), vec.getY(), vec.getZ()).getBlock())).findFirst();

            if (optional.isPresent()) {
                Vector vec = optional.get();
                Block block = new Location(e.getPlayer().getWorld(), vec.getX(), vec.getY(), vec.getZ()).getBlock();
                if (!BlockUtils.isChest(block)) {
                    flag(data, "t: a block: " + block.getType().name(), 1, true, true);
                    cancel(data);
                }
            } else {
                flag(data, "t: b", 1, true, true);
                cancel(data);
            }

        }
    }

    private void cancel(PlayerData data) {
        if (this.isCancellable() && getParentCheck().isCancellable()) {
            data.player.closeInventory();
        }
    }
}
