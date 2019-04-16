package me.rida.anticheat.detections;

import me.rida.anticheat.AntiCheat;
import me.rida.anticheat.data.PlayerData;
import me.rida.anticheat.events.custom.AntiCheatAlertEvent;
import me.rida.anticheat.events.custom.AntiCheatCancelEvent;
import me.rida.anticheat.events.custom.AntiCheatCheatEvent;
import me.rida.anticheat.utils.*;
import com.ngxdev.tinyprotocol.api.ProtocolVersion;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

import java.util.*;

@Getter
@Setter
public abstract class Detection implements Listener {

    private Check parentCheck;
    private String id;
    private boolean enabled;
    private boolean executable;
    private ProtocolVersion versionMinimum, versionMaxmimum;
    private boolean cancellable;
    private int tpsToCancel = 12;
    private int ticks = 0;
    private int thresholdToFlag;
    private boolean experimental;
    private String nickname;
    private boolean bypassLag = false, bypassServerPos = false;
    private Map<String, Object> configValues = new HashMap<>();

    public Detection(Check parentCheck, String id, boolean enabled, boolean executable) {
        this.parentCheck = parentCheck;
        this.id = id;
        this.enabled = enabled;
        this.executable = executable;

        experimental = false;
        cancellable = false;

        thresholdToFlag = 10;

        if (parentCheck.isNeedsListener()) {
            Bukkit.getPluginManager().registerEvents(this, AntiCheat.getInstance());
        }
    }

    public Detection(Check parentCheck, String id, boolean enabled, boolean executable, boolean cancellable) {
        this.parentCheck = parentCheck;
        this.id = id;
        this.enabled = enabled;
        this.executable = executable;
        this.cancellable = cancellable;

        experimental = false;
        thresholdToFlag = 10;

        if (parentCheck.isNeedsListener()) {
            Bukkit.getPluginManager().registerEvents(this, AntiCheat.getInstance());
        }
    }

    public void addConfigValue(String name, Object object) {
        configValues.put(name, object);
    }

    public void setThreshold(int threshold) {
        this.thresholdToFlag = threshold;
    }

    public void debug(PlayerData data, String string) {
        if (data.checkDebugEnabled
                && data.debugDetection == this) {
            AntiCheat.getInstance().debuggingPlayer.sendMessage(Color.Gray + "DEBUG: " + string);
        }
    }

    public void onFunkeEvent(Object event, PlayerData data) {
        //Empty body.
    }

    public void onBukkitEvent(Event event, PlayerData data) {
        //Empty body.
    }

    public void flag(PlayerData dataPlayer, String data, int violations, boolean reliabilitySystem, boolean cancel) {
        if (!dataPlayer.player.hasPermission(Config.bypassPermission)
                || !Config.bypassEnabled) {

            if (AntiCheat.getInstance().tps > 12) {
                float toAdd = (dataPlayer.lastFlag.hasNotPassed(3) ? 4 * violations : violations) * (dataPlayer.reliabilityPercentage / 100);
                List<Violation> violationsList = AntiCheat.getInstance().getCheckManager().violations.getOrDefault(dataPlayer.player.getUniqueId(), new ArrayList<>());
                Optional<Violation> violationOp = violationsList.stream().filter(vl -> vl.getCheck().getName().equals(getParentCheck().getName())).findFirst();

                Violation violation = violationOp.orElseGet(() -> new Violation(getParentCheck()));

                violationsList.remove(violation);
                violation.addViolation(this, toAdd);
                violationsList.add(violation);

                AntiCheatCheatEvent event = new AntiCheatCheatEvent(dataPlayer.player, this, data, violation.getCombinedAmount());
                Bukkit.getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    AntiCheat.getInstance().getCheckManager().violations.put(dataPlayer.player.getUniqueId(), violationsList);
                    if (violation.getSpecificViolations().getOrDefault(this, 0f) > thresholdToFlag) {
                        if (cancel && parentCheck.isCancellable() && isCancellable() && violation.getCombinedAmount() > parentCheck.getCancelThreshold()) {
                            AntiCheatCancelEvent e = new AntiCheatCancelEvent(dataPlayer.player, parentCheck, parentCheck.getType());

                            Bukkit.getPluginManager().callEvent(e);
                            if (!e.isCancelled()) {
                                dataPlayer.setCancelled(parentCheck.getType(), 1);
                            }
                        }
                        JsonMessage jsonMessage = new JsonMessage();
                        JsonMessage.AMText alertMsg = jsonMessage.addText(AntiCheat.getInstance().getMessageFields().alertMessage.replaceAll("%playerName%", dataPlayer.player.getName()).replaceAll("%check%", parentCheck.getName() + (isExperimental() ? Color.Gray + Color.Italics + "(" + getId() + ")" : "(" + getId() + ")")).replaceAll("%vl%", String.valueOf(MathUtils.trim(2, violation.getCombinedAmount()))).replaceAll("%info%", Color.translate(data)).replaceAll("%ping%", dataPlayer.ping + "ms").replaceAll("%tps%", String.valueOf(MathUtils.round(AntiCheat.getInstance().tps, 2)))).addHoverText(AntiCheat.getInstance().getMessageFields().alertHoverMessage.replaceAll("%playerName%", dataPlayer.player.getName()).replaceAll("%info%", Color.translate(data)).replaceAll("%vl%", String.valueOf(MathUtils.trim(2, violation.getCombinedAmount()))).replaceAll("%ping%", dataPlayer.ping + "ms").replaceAll("%tps%", String.valueOf(MathUtils.round(AntiCheat.getInstance().tps, 2))));

                        AntiCheatAlertEvent e = new AntiCheatAlertEvent(dataPlayer.player, this, alertMsg.getMessage());
                        Bukkit.getPluginManager().callEvent(e);
                        if (!e.isCancelled()) {
                            if (!parentCheck.isTestMode()) {
                                if (Config.alertsDelayEnabled) {
                                    if (MathUtils.elapsed(parentCheck.getLastAlert(), Config.alertsDelayMillis)) {

                                        AntiCheat.getInstance().getDataManager().getDataObjects().stream().filter(staffPlayer -> staffPlayer.alerts && MiscUtils.hasPermissionForAlerts(staffPlayer.player)).forEach(staffPlayer -> {
                                            alertMsg.setClickEvent(JsonMessage.ClickableType.RunCommand, Config.alertsCommand.replaceAll("%sender%", staffPlayer.player.getName()).replaceAll("%cheater%", dataPlayer.player.getName()));
                                            jsonMessage.sendToPlayer(staffPlayer.player);
                                        });
                                        parentCheck.setLastAlert(System.currentTimeMillis());
                                    }
                                } else {
                                    if (!e.isCancelled()) {
                                        AntiCheat.getInstance().getDataManager().getDataObjects().stream().filter(staffPlayer -> staffPlayer.alerts && MiscUtils.hasPermissionForAlerts(staffPlayer.player)).forEach(staffPlayer -> {
                                            alertMsg.setClickEvent(JsonMessage.ClickableType.RunCommand, Config.alertsCommand.replaceAll("%sender%", staffPlayer.player.getName()).replaceAll("%cheater%", dataPlayer.player.getName()));
                                            jsonMessage.sendToPlayer(staffPlayer.player);
                                        });
                                    }
                                }
                            } else {
                                if (!dataPlayer.player.hasPermission("anticheat.admin") || !dataPlayer.alerts) {
                                    jsonMessage.sendToPlayer(dataPlayer.player);
                                }
                                if (MathUtils.elapsed(parentCheck.getLastAlert(), Config.alertsDelayMillis)
                                        || !Config.alertsDelayEnabled) {
                                    AntiCheat.getInstance().getDataManager().getDataObjects().stream().filter(staffPlayer -> staffPlayer.alerts && MiscUtils.hasPermissionForAlerts(staffPlayer.player)).forEach(staffPlayer -> {
                                        alertMsg.setClickEvent(JsonMessage.ClickableType.RunCommand, Config.alertsCommand.replaceAll("%sender%", staffPlayer.player.getName()).replaceAll("%cheater%", dataPlayer.player.getName()));
                                        jsonMessage.sendToPlayer(staffPlayer.player);
                                    });
                                    parentCheck.setLastAlert(System.currentTimeMillis());
                                }
                            }
                        }
                    }
                    dataPlayer.lastFlag.reset();
                    AntiCheat.getInstance().getDataManager().getLogger().addLog(this, violation.getSpecificViolations().get(this), data, dataPlayer);
                }
            } else {
                String message = AntiCheat.getInstance().getMessageFields().prefix + Color.translate("&e" + dataPlayer.player.getName() + " &7would have flagged &e" + parentCheck.getName() + "(" + getId() + ") &7but the server lagged. &8[&c" + MathUtils.round(AntiCheat.getInstance().tps, 3) + "&8]");
                AntiCheat.getInstance().getDataManager().getDataObjects().stream().filter(staffPlayer -> staffPlayer.alerts && MiscUtils.hasPermissionForAlerts(staffPlayer.player)).forEach(staffPlayer -> staffPlayer.player.sendMessage(message));
            }

            if (!reliabilitySystem || dataPlayer.reliabilityPercentage > 50.0) {
                parentCheck.checkBan(dataPlayer, this);
            }
        }
    }
}
