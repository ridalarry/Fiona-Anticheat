package me.rida.anticheat.events.bukkit;

import me.rida.anticheat.AntiCheat;
import me.rida.anticheat.data.PlayerData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRiptideEvent;

public class CancerListeners implements Listener {
    @EventHandler
    public void onEvent(PlayerRiptideEvent event) {
        PlayerData data = AntiCheat.getInstance().getDataManager().getPlayerData(event.getPlayer());

        data.riptideTicks+= 5;
    }
}
