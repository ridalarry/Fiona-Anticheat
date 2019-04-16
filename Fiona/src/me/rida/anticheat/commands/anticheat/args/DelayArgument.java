package me.rida.anticheat.commands.anticheat.args;

import me.rida.anticheat.AntiCheat;
import me.rida.anticheat.commands.FunkeArgument;
import me.rida.anticheat.utils.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class DelayArgument extends FunkeArgument {

    public DelayArgument() {
        super("delay", "delay <boolean/millis>", "modify the delay between alerts sent.", "anticheat.delay");

        addTabComplete(2, "false");
        addTabComplete(2, "true");
    }


    @Override
    public void onArgument(CommandSender sender, Command cmd, String[] args) {
        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("true")
                    || args[1].equals("false")) {
                boolean enabled = Boolean.parseBoolean(args[1]);
                AntiCheat.getInstance().getConfig().set("alerts.delay.enabled", enabled);
                AntiCheat.getInstance().saveConfig();
                AntiCheat.getInstance().reloadConfigObject();
                sender.sendMessage(AntiCheat.getInstance().getMessageFields().prefix + (enabled ? Color.Green + "Enabled the delay between alerts." : Color.Red + "Disabled the delay between alerts."));
            } else {
                try {
                    long millis = Long.parseLong(args[1]);
                    AntiCheat.getInstance().getConfig().set("alerts.delay.millis", millis);
                    AntiCheat.getInstance().saveConfig();
                    AntiCheat.getInstance().reloadConfigObject();
                    sender.sendMessage(AntiCheat.getInstance().getMessageFields().prefix + Color.Gray + "Set the alerts delay to: " + Color.White + millis + Color.Gray + "ms");
                } catch (Exception e) {
                    sender.sendMessage(AntiCheat.getInstance().getMessageFields().invalidArguments);
                }
            }
        } else {
            sender.sendMessage(AntiCheat.getInstance().getMessageFields().invalidArguments);
        }
    }
}
