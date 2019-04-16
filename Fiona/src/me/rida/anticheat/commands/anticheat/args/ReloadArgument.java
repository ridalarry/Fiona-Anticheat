package me.rida.anticheat.commands.anticheat.args;

import me.rida.anticheat.AntiCheat;
import me.rida.anticheat.commands.FunkeArgument;
import me.rida.anticheat.utils.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ReloadArgument
        extends FunkeArgument {
    public ReloadArgument() {
        super("reload", "reload <full/config/data>", "reload different parts of AntiCheat.", "anticheat.reload");

        addTabComplete(2, "full");
        addTabComplete(2, "partial");
        addTabComplete(2, "config");
        addTabComplete(2, "data");
    }

    @Override
    public void onArgument(CommandSender sender, Command command, String[] args) {
        if (args.length == 2) {
            sender.sendMessage(AntiCheat.getInstance().getMessageFields().prefix + Color.Gray + "Working...");
            switch (args[1]) {
                case "full": {
                    sender.sendMessage(AntiCheat.getInstance().getMessageFields().prefix + Color.Gray + "Reloading configurations...");
                    AntiCheat.getInstance().reloadConfig();
                    AntiCheat.getInstance().reloadConfigObject();
                    AntiCheat.getInstance().reloadMessages();
                    AntiCheat.getInstance().reloadMessagesObject();
                    sender.sendMessage(AntiCheat.getInstance().getMessageFields().prefix + Color.Gray + "Reloading data objects...");
                    AntiCheat.getInstance().getCheckManager().violations.clear();
                    AntiCheat.getInstance().reloadPlayerData();
                    AntiCheat.getInstance().getCheckManager().getChecks().clear();
                    AntiCheat.getInstance().getCheckManager().initializeDetections();
                    AntiCheat.getInstance().loadChecks();
                    break;
                }
                case "config": {
                    sender.sendMessage(AntiCheat.getInstance().getMessageFields().prefix + Color.Gray + "Reloading configurations...");
                    AntiCheat.getInstance().reloadConfig();
                    AntiCheat.getInstance().reloadConfigObject();
                    AntiCheat.getInstance().reloadPlayerData();
                    AntiCheat.getInstance().reloadMessages();
                    AntiCheat.getInstance().reloadMessagesObject();
                    break;
                }
                case "data": {
                    sender.sendMessage(AntiCheat.getInstance().getMessageFields().prefix + Color.Gray + "Reloading data objects...");
                    AntiCheat.getInstance().reloadPlayerData();
                    AntiCheat.getInstance().getCheckManager().violations.clear();
                    break;
                }
                default: {
                    sender.sendMessage(AntiCheat.getInstance().getMessageFields().prefix + Color.translate("&c&oIncorrect argument \"" + args[1] + "\". Defaulting to &f&ofull&c&o."));
                    sender.sendMessage(AntiCheat.getInstance().getMessageFields().prefix + Color.Gray + "Reloading configurations...");
                    AntiCheat.getInstance().reloadConfig();
                    AntiCheat.getInstance().reloadConfigObject();
                    AntiCheat.getInstance().reloadMessages();
                    AntiCheat.getInstance().reloadMessagesObject();
                    sender.sendMessage(AntiCheat.getInstance().getMessageFields().prefix + Color.Gray + "Reloading data objects...");
                    AntiCheat.getInstance().reloadPlayerData();
                    AntiCheat.getInstance().getCheckManager().getChecks().clear();
                    AntiCheat.getInstance().loadChecks();
                    AntiCheat.getInstance().getCheckManager().violations.clear();
                    break;
                }
            }
            sender.sendMessage(AntiCheat.getInstance().getMessageFields().prefix + Color.Green + "Done!");
        } else {
            sender.sendMessage(AntiCheat.getInstance().getMessageFields().prefix + Color.translate("&cInvalid Arguments! &7Options: &ffull&7, &fconfig&7, &fdata&7."));
        }
    }
}

