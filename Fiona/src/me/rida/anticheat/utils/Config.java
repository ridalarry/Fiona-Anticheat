package me.rida.anticheat.utils;

import me.rida.anticheat.AntiCheat;
import me.rida.anticheat.data.logging.DatabaseType;

public class Config {
    public static String alertsCommand, bypassPermission, logTimeUnit, animationType, mongoAddress, mongoDatabase;
    public static DatabaseType databaseType;
    public static long alertsDelayMillis, saveInterval;
    public static boolean alertsDelayEnabled, checkVioResetBroadcast, bypassEnabled, logging;
    public static int maxMove, clickThreshold, mongoPort;

    public Config() {
        alertsCommand = getString("alerts.clickCommand");
        alertsDelayEnabled = getBoolean("alerts.delay.enabled");
        alertsDelayMillis = getLong("alerts.delay.millis");
        checkVioResetBroadcast = getBoolean("alerts.checkVioResetBroadcast");
        bypassPermission = getString("bypass.permission");
        bypassEnabled = getBoolean("bypass.enabled");
        logging = getBoolean("logging.enabled");
        saveInterval = getLong("logging.saveInterval");
        logTimeUnit = getString("logging.timeUnit").toUpperCase();
        maxMove = getInt("checks.BadPackets.TypeC.maxMove");
        clickThreshold = getInt("checks.AutoClicker.TypeA.clickThreshold");
        animationType = getString("gui.animation");
        databaseType = DatabaseType.getByName(getString("database.type"));
        mongoAddress = getString("database.mongo.address");
        mongoDatabase = getString("database.mongo.name");
        mongoPort = getInt("data.basemongo.port");

    }

    private String getString(String path) {
        return AntiCheat.getInstance().getConfig().getString(path);
    }

    private long getLong(String path) {
        return AntiCheat.getInstance().getConfig().getLong(path);
    }

    private int getInt(String path) {
        return AntiCheat.getInstance().getConfig().getInt(path);
    }

    private boolean getBoolean(String path) {
        return AntiCheat.getInstance().getConfig().getBoolean(path);
    }
}

