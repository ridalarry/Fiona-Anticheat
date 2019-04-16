package me.rida.anticheat.commands.anticheat.args;

import me.rida.anticheat.AntiCheat;
import me.rida.anticheat.commands.FunkeArgument;
import me.rida.anticheat.utils.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MenuArgument extends FunkeArgument {
    public MenuArgument() {
        super("menu", "menu", "Open the AntiCheat GUI.", "anticheat.menu");

        addAlias("gui");
    }

    @Override
    public void onArgument(CommandSender sender, Command cmd, String[] args) {
        if (sender instanceof Player) {
            AntiCheat.getInstance().getGuiManager().openInventory((Player) sender, "AntiCheat-Menu");
        } else {
            sender.sendMessage(AntiCheat.getInstance().getMessageFields().prefix + Color.Red + "Only players can use this command.");
        }
    }
}
