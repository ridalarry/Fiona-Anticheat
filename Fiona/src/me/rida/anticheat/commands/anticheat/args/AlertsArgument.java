package me.rida.anticheat.commands.anticheat.args;

import me.rida.anticheat.AntiCheat;
import me.rida.anticheat.commands.FunkeArgument;
import me.rida.anticheat.data.PlayerData;
import me.rida.anticheat.utils.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AlertsArgument
        extends FunkeArgument {
    public AlertsArgument() {
        super("alerts", "alerts", "toggle alerts on or off.", "anticheat.alerts");

        addTabComplete(2, "true");
        addTabComplete(2, "false");
    }

    @Override
    public void onArgument(CommandSender sender, Command command, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PlayerData data = AntiCheat.getInstance().getDataManager().getPlayerData(player);
            if (data == null) {
                player.sendMessage(AntiCheat.getInstance().getMessageFields().prefix + Color.Red + "Unknown error occured where your data object returns null.");
                return;
            }
            data.alerts = !data.alerts;
            player.sendMessage(AntiCheat.getInstance().getMessageFields().prefix + Color.Gray + "Set your alerts mode to " + (data.alerts ? Color.Green + "true" : Color.Red + "false") + Color.Gray + " !");
        } else {
            sender.sendMessage(AntiCheat.getInstance().getMessageFields().prefix + Color.Red + "You must be a player to use this command!");
        }
    }
}

