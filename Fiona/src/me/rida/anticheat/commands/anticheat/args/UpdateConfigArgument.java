package me.rida.anticheat.commands.anticheat.args;

import me.rida.anticheat.AntiCheat;
import me.rida.anticheat.commands.FunkeArgument;
import me.rida.anticheat.utils.MiscUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.time.LocalDateTime;

public class UpdateConfigArgument extends FunkeArgument {

    public UpdateConfigArgument() {
        super("updateConfig", "updateConfig <full, checks, checkValues>", "Update the config to the recommended defaults.", "anticheat.updateConfig");

        addTabComplete(2, "full");
        addTabComplete(2, "all");
        addTabComplete(2, "checks");
        addTabComplete(2, "check");
        addTabComplete(2, "detection");
        addTabComplete(2, "detections");
        addTabComplete(2, "checkvalues");
    }

    @Override
    public void onArgument(CommandSender sender, Command cmd, String[] args) {
        if (args.length == 1) {
            sender.sendMessage(AntiCheat.getInstance().getMessageFields().invalidArguments);
        } else {
            switch (args[1].toLowerCase()) {
                case "full":
                case "all": {
                    sender.sendMessage(MiscUtils.formattedMessage("&7Updating AntiCheat configuration to it's recommended defaults..."));
                    File config = new File(AntiCheat.getInstance().getDataFolder(), "config.yml");

                    LocalDateTime now = LocalDateTime.now();
                    File oldConfig = new File(AntiCheat.getInstance().getDataFolder(), "config_old_" + now + ".yml");

                    if (config.renameTo(oldConfig)) {
                        sender.sendMessage(MiscUtils.formattedMessage("&7Old configuration saved as: &e" + oldConfig.getName()));
                    } else {
                        sender.sendMessage(MiscUtils.formattedMessage("&cError occurred trying to make back-up of your old configuration!") + "\n" + MiscUtils.formattedMessage("&7&oYou can do /anticheat " + args[1] + " force to continue anyway."));
                        return;
                    }
                    AntiCheat.getInstance().saveDefaultConfig();
                    sender.sendMessage(MiscUtils.formattedMessage("&7Updated the default config. Reloading AntiCheat..."));
                    if (sender.hasPermission("anticheat.reload") || sender.hasPermission("anticheat.admin")) {
                        Bukkit.dispatchCommand(sender, "anticheat reload full");
                    } else {
                        Bukkit.dispatchCommand(AntiCheat.getInstance().consoleSender, "anticheat reload full");
                    }
                    MiscUtils.formattedMessage("&aDone!");

                    break;
                }
                case "checks":
                case "check":
                case "detections":
                case "detection": {
                    sender.sendMessage(MiscUtils.formattedMessage("&7Updating the check section to its recommended default..."));
                    AntiCheat.getInstance().getConfig().set("checks", null);
                    AntiCheat.getInstance().saveConfig();
                    AntiCheat.getInstance().getCheckManager().getChecks().clear();
                    AntiCheat.getInstance().getCheckManager().initializeDetections();
                    sender.sendMessage(MiscUtils.formattedMessage("&7Removed previous section. Setting up new section..."));
                    AntiCheat.getInstance().loadChecks();
                    sender.sendMessage(MiscUtils.formattedMessage("&aDone!"));
                    break;
                }
                case "checkvalues":
                case "values": {
                    sender.sendMessage(MiscUtils.formattedMessage("&7Updating the check values to their recommended defaults..."));
                    AntiCheat.getInstance().getCheckManager().getChecks().forEach(check -> {
                        String checkPath = "checks." + check.getName() + ".";
                        AntiCheat.getInstance().getConfig().set(checkPath + "values", null);

                        check.getDetections().forEach(detection -> AntiCheat.getInstance().getConfig().set(checkPath + "detections." + detection.getId() + ".values", null));
                    });
                    AntiCheat.getInstance().saveConfig();
                    AntiCheat.getInstance().getCheckManager().getChecks().clear();
                    AntiCheat.getInstance().getCheckManager().initializeDetections();
                    sender.sendMessage(MiscUtils.formattedMessage("&7Reset values. Setting defaults..."));
                    AntiCheat.getInstance().reloadConfig();
                    AntiCheat.getInstance().loadChecks();
                    sender.sendMessage(MiscUtils.formattedMessage("&aDone!"));
                    break;
                }
                default: {
                    sender.sendMessage(AntiCheat.getInstance().getMessageFields().invalidArguments);
                    break;
                }
            }
        }
    }
}
