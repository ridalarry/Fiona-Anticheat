package me.rida.anticheat.commands.anticheat.args;

import me.rida.anticheat.AntiCheat;
import me.rida.anticheat.commands.FunkeArgument;
import me.rida.anticheat.utils.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetHungerArgument extends FunkeArgument {

    public SetHungerArgument() {
        super("sethunger", "sethunger <amount>", "to change your hunger (funke cmd).", "anticheat.sethunger");
    }

    @Override
    public void onArgument(CommandSender sender, Command cmd, String[] args) {
        if (args.length == 2
                && sender instanceof Player) {
            Player player = (Player) sender;
            int amount = Integer.parseInt(args[1]);

            player.setFoodLevel(amount);
            sender.sendMessage(Color.Gray + "Set food level to: " + Color.Yellow + player.getFoodLevel());
        } else {
            sender.sendMessage(AntiCheat.getInstance().getMessageFields().prefix + AntiCheat.getInstance().getMessageFields().invalidArguments);
        }
    }
}
