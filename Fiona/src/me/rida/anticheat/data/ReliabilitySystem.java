package me.rida.anticheat.data;

import me.rida.anticheat.AntiCheat;

import java.util.concurrent.TimeUnit;

public class ReliabilitySystem {

    public ReliabilitySystem() {
        AntiCheat.getInstance().executorService.scheduleAtFixedRate(() -> {
            AntiCheat.getInstance().getDataManager().getDataObjects().forEach(ReliabilitySystem::handle);
        }, 0L, 50L, TimeUnit.MILLISECONDS);
    }

    public static void handle(PlayerData data) {
        if (data.hasLag() && data.lastLogin.hasPassed(150)) {
            data.reliabilityPoints += 1;
            //Bukkit.broadcastMessage(data.reliabilityPoints + " (+2)");
        }
        data.reliabilityPoints = Math.max(data.reliabilityPoints - 0.05f, 0);
        data.reliabilityPercentage = 100 - Math.min(100, data.reliabilityPoints / 20);
        //Bukkit.broadcastMessage(data.lastFlyingPacketDif + ", " + Math.abs(data.lastPing - data.ping) + ", " + data.hasLag()  + ", " + data.reliabilityPoints);
        //Bukkit.broadcastMessage(data.reliabilityPercentage + "%");
    }
}
