package me.rida.anticheat.detections;

import me.rida.anticheat.AntiCheat;
import me.rida.anticheat.data.PlayerData;
import me.rida.anticheat.events.custom.AntiCheatPunishEvent;
import me.rida.anticheat.utils.Color;
import me.rida.anticheat.utils.Config;
import me.rida.anticheat.utils.MiscUtils;
import me.rida.anticheat.utils.Violation;
import com.google.common.collect.Lists;
import com.ngxdev.tinyprotocol.api.ProtocolVersion;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public abstract class Check implements Listener {
    private static Check instance;
    private String name;
    private CheckType type;
    private boolean enabled;
    private boolean cancellable;
    private int cancelThreshold;
    private int executableThreshold;
    private long lastAlert = 0;
    private boolean testMode;
    private boolean executable;
    private String nickName;
    private boolean needsListener;
    private List<Detection> detections;
    private Map<String, Object> configValues = new HashMap<>();


    public Check(String name, CheckType type, boolean enabled, boolean executable, boolean cancellable, boolean needsListener, int executableThreshold, int cancelThreshold) {
        this.name = name;
        this.type = type;
        this.enabled = enabled;
        this.executable = executable;
        this.cancellable = cancellable;
        this.cancelThreshold = cancelThreshold;
        this.needsListener = needsListener;
        this.executableThreshold = executableThreshold;
        this.executableThreshold = executableThreshold;
        this.detections = new CopyOnWriteArrayList<>();

        instance = this;

        testMode = AntiCheat.getInstance().getConfig().getBoolean("testmode");
    }

    public Check(String name, CheckType type, boolean enabled, boolean executable, boolean cancellable, boolean needsListener, int executableThreshold, long millis, int cancelThreshold) {
        this.name = name;
        this.type = type;
        this.enabled = enabled;
        this.executable = executable;
        this.cancellable = cancellable;
        this.cancelThreshold = cancelThreshold;
        this.executableThreshold = executableThreshold;
        this.needsListener = needsListener;
        this.detections = new CopyOnWriteArrayList<>();

        instance = this;

        AntiCheat.getInstance().executorService.scheduleAtFixedRate(() -> {
            AntiCheat.getInstance().profile.start("Task:CheckReset:" + name);

            if (Config.checkVioResetBroadcast) {
                for (PlayerData data : AntiCheat.getInstance().getDataManager().getDataObjects()) {
                    if (data.alerts && MiscUtils.hasPermissionForAlerts(data.player)) {
                        data.player.sendMessage(AntiCheat.getInstance().getMessageFields().checkVioReset.replaceAll("%check%", name));
                    }
                    List<Violation> violations = AntiCheat.getInstance().getCheckManager().violations.getOrDefault(data.player.getUniqueId(), Lists.newArrayList());

                    if(violations.size() > 0) {
                        violations.stream().filter(vio -> vio.getCheck().getName().equals(getName())).forEach(violations::remove);

                        AntiCheat.getInstance().getCheckManager().violations.put(data.player.getUniqueId(), violations);
                    }
                }
            }
            AntiCheat.getInstance().profile.stop("Task:CheckReset:" + name);
        }, 1L, millis, TimeUnit.MILLISECONDS);

        testMode = AntiCheat.getInstance().getConfig().getBoolean("testmode");
    }


    public static Check getInstance() {
        return instance;
    }

    public void addConfigValue(String name, Object object) {
        configValues.put(name, object);
    }

    public void addDetection(Detection detection) {
        if (detection.getVersionMinimum() != null) {
            if (ProtocolVersion.getGameVersion().isOrAbove(detection.getVersionMinimum())) {
                detections.add(detection);
                MiscUtils.printToConsole(Color.Green + getName() + "(" + detection.getId() + ") was added since you are using " + Color.Yellow + ProtocolVersion.getGameVersion().getServerVersion());
            } else
                MiscUtils.printToConsole(Color.Red + getName() + "(" + detection.getId() + ") was not added since you are using " + Color.Yellow + ProtocolVersion.getGameVersion().getServerVersion());
        } else if (detection.getVersionMaxmimum() != null) {
            if (ProtocolVersion.getGameVersion().isBelow(detection.getVersionMaxmimum()) || ProtocolVersion.getGameVersion().equals(detection.getVersionMaxmimum())) {
                detections.add(detection);
                MiscUtils.printToConsole(Color.Green + getName() + "(" + detection.getId() + ") was added since you are using " + Color.Yellow + ProtocolVersion.getGameVersion().getServerVersion());
            } else
                MiscUtils.printToConsole(Color.Red + getName() + "(" + detection.getId() + ") was not added since you are using " + Color.Yellow + ProtocolVersion.getGameVersion().getServerVersion());
        } else {
            detections.add(detection);
        }
    }

    public Detection getDetectionByName(String name) {
        return detections.stream().filter(detection -> detection.getId().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public boolean isDetection(String name) {
        return detections.stream().anyMatch(detection -> detection.getId().equalsIgnoreCase(name));
    }

    public void setEnabled(boolean enabled) {
        if (needsListener) {
            if (enabled) {
                AntiCheat.getInstance().getServer().getPluginManager().registerEvents(this, AntiCheat.getInstance());
            } else {
                HandlerList.unregisterAll(this);
            }
        }
        this.enabled = enabled;
    }

    public void checkBan(PlayerData data, Detection detection) {
        if (AntiCheat.getInstance().bannedPlayers.contains(data.player)) return;

        if ((!this.isExecutable() || !detection.isExecutable()) || data.getViolations(detection) < this.getExecutableThreshold() || data.reliabilityPercentage < 75)
            return;
        AntiCheatPunishEvent event = new AntiCheatPunishEvent(data.player, this);
        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            ((me.rida.anticheat.data.logging.Yaml) AntiCheat.getInstance().getDataManager().getLogger()).dumpLog(data);
            AntiCheat.getInstance().executeOnPlayer(data.player, this, detection);
        }
        AntiCheat.getInstance().getCheckManager().violations.remove(data.player.getUniqueId());
    }

}

