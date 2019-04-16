package me.rida.anticheat.commands.anticheat.args;

import me.rida.anticheat.AntiCheat;
import me.rida.anticheat.commands.FunkeArgument;
import me.rida.anticheat.detections.Check;
import me.rida.anticheat.detections.Detection;
import me.rida.anticheat.utils.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CancellableArgument
        extends FunkeArgument {
    public CancellableArgument() {
        super("cancellable", "cancellable <check> [detection]", "enable or disable the checks ability to send commands.", "anticheat.cancel");

        addAlias("cancel");
        addAlias("setback");
        addAlias("rubberband");

        addTabComplete(2, "%check%");
        addTabComplete(2, "*");
        addTabComplete(3, "%detection%,!*,2");
        addTabComplete(3, "enable,*,2");
        addTabComplete(3, "disable,*,2");
        addTabComplete(4, "save,*,2");
    }

    @Override
    public void onArgument(CommandSender sender, Command command, String[] args) {
        if(args.length > 1) {
            if (args[1].equals("*") && args.length > 2) {
                if (args[2].equalsIgnoreCase("disable")) {
                    for (Check check : AntiCheat.getInstance().getCheckManager().getChecks()) {
                        check.setCancellable(false);
                        check.getDetections().forEach(detection -> detection.setCancellable(false));
                    }
                    if(args.length > 3 && args[3].equalsIgnoreCase("save")) {
                        AntiCheat.getInstance().saveChecks();
                    }
                    sender.sendMessage(AntiCheat.getInstance().getMessageFields().prefix + Color.Green + "Disabled all checks' cancelling abilities!");
                    return;
                } else if (args[2].equalsIgnoreCase("enable")) {
                    for (Check check : AntiCheat.getInstance().getCheckManager().getChecks()) {
                        check.setCancellable(true);
                        check.getDetections().forEach(detection -> detection.setCancellable(true));
                    }

                    if(args.length > 3 && args[3].equalsIgnoreCase("save")) {
                        AntiCheat.getInstance().saveChecks();
                    }
                    sender.sendMessage(AntiCheat.getInstance().getMessageFields().prefix + Color.Green + "Enabled all checks' cancelling abilities!");
                    return;
                }
            } else if(AntiCheat.getInstance().getCheckManager().isCheck(args[1])) {
                Check check = AntiCheat.getInstance().getCheckManager().getCheckByName(args[1]);

                if(args.length == 3) {
                    String toCheck = args[2].replaceAll("_", " ");
                    if(check.isDetection(toCheck)) {
                        Detection detection = check.getDetectionByName(toCheck);

                        detection.setCancellable(!detection.isCancellable());

                        sender.sendMessage(Color.translate(
                                AntiCheat.getInstance().getMessageFields().prefix + " &7The check &c" + check.getName() + "&7(&e" + detection.getId() + "&7)'s cancelling abilities have been " + (detection.isCancellable() ? "&aenabled" : "&cdisabled") + "&7."));

                    } else {
                        sender.sendMessage(AntiCheat.getInstance().getMessageFields().prefix + Color.Red + "The detection \"" + toCheck + "\" does not exist!");
                    }

                } else {
                    check.setCancellable(!check.isCancellable());
                    sender.sendMessage(Color.translate(
                            AntiCheat.getInstance().getMessageFields().prefix + " &7The check &c" + check.getName() + "&7's cancelling abilities have been " + (check.isCancellable() ? "&aenabled" : "&cdisabled") + "&7."));
                }
                AntiCheat.getInstance().saveChecks();
            } else {
                sender.sendMessage(AntiCheat.getInstance().getMessageFields().prefix + Color.Red + "The check \"" + args[1] + "\" does not exist!");
            }
            return;
        }
        sender.sendMessage(AntiCheat.getInstance().getMessageFields().invalidArguments);
    }
}

