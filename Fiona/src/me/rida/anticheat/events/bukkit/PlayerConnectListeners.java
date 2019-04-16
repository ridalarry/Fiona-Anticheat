package me.rida.anticheat.events.bukkit;

import me.rida.anticheat.AntiCheat;
import me.rida.anticheat.data.PlayerData;
import me.rida.anticheat.data.logging.Yaml;
import com.ngxdev.tinyprotocol.api.TinyProtocolHandler;
import com.ngxdev.tinyprotocol.packet.out.WrappedOutTransaction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerConnectListeners
        implements Listener {
    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent e) {
        AntiCheat.getInstance().getDataManager().createDataObject(e.getPlayer());
        AntiCheat.getInstance().getDataManager().getPlayerData(e.getPlayer()).lastLogin.reset();
        new BukkitRunnable() {
            public void run() {
                TinyProtocolHandler.sendPacket(e.getPlayer(), new WrappedOutTransaction(0, (short) 69, true));
            }
        }.runTaskLater(AntiCheat.getInstance(), 25L);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent e) {
        PlayerData data = AntiCheat.getInstance().getDataManager().getPlayerData(e.getPlayer());

        ((Yaml) AntiCheat.getInstance().getDataManager().getLogger()).dumpLog(data);
        AntiCheat.getInstance().getDataManager().removeDataObject(AntiCheat.getInstance().getDataManager().getPlayerData(e.getPlayer()));
        AntiCheat.getInstance().getBannedPlayers().remove(e.getPlayer());
    }
}

