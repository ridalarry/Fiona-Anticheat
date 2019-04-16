package me.rida.anticheat.commands.anticheat.args;

import me.rida.anticheat.AntiCheat;
import me.rida.anticheat.commands.FunkeArgument;
import me.rida.anticheat.data.logging.Yaml;
import me.rida.anticheat.utils.Color;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class SaveArgument extends FunkeArgument {
    public SaveArgument() {
        super("savelogs", "savelogs", "Force-save the logs of player's to a Yaml file.", "anticheat.save");

        addAlias("forcesave");
        addAlias("save");
        addAlias("sl");
        addAlias("slogs");
    }

    @Override
    public void onArgument(CommandSender sender, Command cmd, String[] args) {
        if (AntiCheat.getInstance().getDataManager().getLogger() instanceof Yaml) {
            sender.sendMessage(AntiCheat.getInstance().getMessageFields().prefix + Color.Red + "Saving all violations logs...");
            Yaml logger = (Yaml) AntiCheat.getInstance().getDataManager().getLogger();

            logger.dumpLogs();
            sender.sendMessage(AntiCheat.getInstance().getMessageFields().prefix + Color.Green + "Saved!");
        } else {
            sender.sendMessage(ChatColor.RED + "This feature is only for YAML logging since it is useless in other forms.");
        }
    }
}
