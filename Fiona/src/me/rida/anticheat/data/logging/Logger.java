package me.rida.anticheat.data.logging;

import me.rida.anticheat.data.PlayerData;
import me.rida.anticheat.detections.Detection;

import java.util.List;
import java.util.UUID;

public interface Logger {

    void addLog(Detection detection, double vl, String info, PlayerData data);

    List<Log> getLogs(UUID uuid);
}
