package me.rida.anticheat.events.custom;

import me.rida.anticheat.detections.Detection;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AntiCheatAlertEvent extends Event implements Cancellable {
    private static HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private Player player;
    private Detection detection;
    private String alertMessage;

    public AntiCheatAlertEvent(Player player, Detection detection, String alertMessage) {
        this.player = player;
        this.detection = detection;
        this.alertMessage = alertMessage;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public Detection getDetection() {
        return detection;
    }

    public String getAlertMessage() {
        return alertMessage;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}