package me.rida.anticheat.data;

import me.rida.anticheat.AntiCheat;
import me.rida.anticheat.data.logging.DatabaseType;
import me.rida.anticheat.data.logging.Logger;
import me.rida.anticheat.data.logging.Yaml;
import me.rida.anticheat.utils.Config;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class DataManager {

    /**
     * Player Object Stuff
     **/
    private final List<PlayerData> dataObjects;
    @Getter
    private Logger logger;

    public DataManager() {
        dataObjects = new ArrayList<>();
        logger = Config.databaseType.equals(DatabaseType.MONGO) ? new Yaml() : new Yaml();

        new BukkitRunnable() {
            public void run() {
                dataObjects.forEach(data -> {
                    if (data.onGround) {
                        data.serverGroundTicks++;
                        data.serverAirTicks = 0;
                    } else {
                        data.serverGroundTicks = 0;
                        data.serverAirTicks++;
                    }
                });
            }
        }.runTaskTimerAsynchronously(AntiCheat.getInstance(), 1L, 1L);
    }

    public void createDataObject(Player player) {
        dataObjects.add(new PlayerData(player));
    }

    public void removeDataObject(PlayerData dataObject) {
        dataObjects.remove(dataObject);
    }

    public PlayerData getPlayerData(Player player) {
        for (PlayerData data : dataObjects) {
            if (data.player == player) return data;
        }
        return null;
    }

    public List<PlayerData> getDataObjects() {
        return dataObjects;
    }

}
