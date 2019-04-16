package me.rida.anticheat.data.logging;

import me.rida.anticheat.AntiCheat;
import me.rida.anticheat.data.PlayerData;
import me.rida.anticheat.detections.Detection;
import me.rida.anticheat.utils.Config;
import me.rida.anticheat.utils.FunkeFile;
import me.rida.anticheat.utils.MathUtils;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Yaml implements Logger {
    public Yaml() {
        startLogSaving();
    }

    @Override
    public void addLog(Detection detection, double vl, String info, PlayerData data) {
        Log log = new Log(data.player.getUniqueId(), detection.getParentCheck().getName(), detection.getId(), info, data.ping, AntiCheat.getInstance().getTps(), vl, MathUtils.round(data.reliabilityPercentage, 5));

        addLogString(data, log.getCheck() + ";" + log.getDetection() + ";" + log.getData() + ";" + log.getPing() + ";" + log.getTps() + ";" + log.getVl() + ";" + log.getReliabilityPercentage());
    }

    @Override
    public List<Log> getLogs(UUID uuid) {
        List<Log> logs = Lists.newArrayList();
        FunkeFile file = new FunkeFile(AntiCheat.getInstance(), "logs", uuid.toString() + ".txt");

        file.readFile();

        file.lines.forEach(line -> {
            String[] split = line.split(";");

            logs.add(new Log(uuid, split[0], split[1], split[2], Integer.parseInt(split[3]), Double.parseDouble(split[4]), Double.parseDouble(split[5]), Double.parseDouble(split[6])));
        });
        return logs;
    }

    public FunkeFile getLogFile(UUID uuid) {
        FunkeFile file = new FunkeFile(AntiCheat.getInstance(), "logs", uuid.toString() + ".txt");

        file.readFile();

        return file;
    }

    private void addLogString(PlayerData data, String string) {
        if (Config.logging) data.cachedLogStrings.add(string);
    }

    public void dumpLogs() {
        AntiCheat.getInstance().getDataManager().getDataObjects().stream().filter(data -> data.cachedLogStrings.size() > 0).forEach(data -> {
            FunkeFile file = new FunkeFile(AntiCheat.getInstance(), "logs", data.player.getUniqueId().toString() + ".txt");
            data.cachedLogStrings.forEach(file::addLine);
            file.write();
        });
    }

    public void dumpLog(PlayerData data) {
        if (data.cachedLogStrings.size() > 0) {
            FunkeFile file = new FunkeFile(AntiCheat.getInstance(), "logs", data.player.getUniqueId().toString() + ".txt");
            data.cachedLogStrings.forEach(file::addLine);
            file.write();
        }
    }

    private void startLogSaving() {
        AntiCheat.getInstance().executorService.scheduleAtFixedRate(this::dumpLogs, 0L, Config.saveInterval, TimeUnit.valueOf(Config.logTimeUnit));
    }

}
