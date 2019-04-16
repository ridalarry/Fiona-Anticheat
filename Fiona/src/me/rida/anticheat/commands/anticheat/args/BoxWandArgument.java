package me.rida.anticheat.commands.anticheat.args;

import me.rida.anticheat.AntiCheat;
import me.rida.anticheat.commands.FunkeArgument;
import me.rida.anticheat.utils.Color;
import me.rida.anticheat.utils.MiscUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BoxWandArgument extends FunkeArgument {
    public BoxWandArgument() {
        super("boxwand", "boxwand", "gives you a wand to see a block's hitbox.", "anticheat.boxwand");

        addAlias("magicwand");
        addAlias("wand");
    }

    @Override
    public void onArgument(CommandSender sender, Command cmd, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            ItemStack item = MiscUtils.createItem(Material.BLAZE_ROD, 1, "&cMagic Box Wand");

            player.getInventory().addItem(item);
            player.updateInventory();
            player.sendMessage(AntiCheat.getInstance().getMessageFields().prefix + Color.Gray + "Here's your magic blockbox wand. Enjoy your day at Shrek Studios.");
        } else {
            sender.sendMessage(AntiCheat.getInstance().getMessageFields().prefix + Color.Red + "You must be a player to use this feature.");
        }
    }
}
