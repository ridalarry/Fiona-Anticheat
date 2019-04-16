package me.rida.anticheat.commands.anticheat.args;

import me.rida.anticheat.AntiCheat;
import me.rida.anticheat.commands.FunkeArgument;
import me.rida.anticheat.profiling.ToggleableProfiler;
import me.rida.anticheat.utils.Color;
import me.rida.anticheat.utils.MathUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

public class ProfileArgument extends FunkeArgument {

    private long profileStart;

    public ProfileArgument() {
        super("profile", "profile", "toggle the timings profile on/off.", "anticheat.profile");
    }

    @Override
    public void onArgument(CommandSender sender, Command cmd, String[] args) {
        if (args.length == 1) {
            AntiCheat.getInstance().profile.enabled = !AntiCheat.getInstance().profile.enabled;
            sender.sendMessage(AntiCheat.getInstance().getMessageFields().prefix + Color.Gray + "Set the timings profiler to: " + Color.Yellow + AntiCheat.getInstance().profile.enabled);
            if (!AntiCheat.getInstance().profile.enabled) {
                long totalTime = MathUtils.elapsed(profileStart);
                sender.sendMessage(Color.Gold + Color.Bold + "AntiCheat Timings:");
                float totalPCT = 0;
                for (String string : AntiCheat.getInstance().profile.total.keySet()) {
                    sender.sendMessage(Color.Red + Color.Underline + string);
                    double stringTotal = TimeUnit.NANOSECONDS.toMillis(AntiCheat.getInstance().profile.total.get(string));
                    int calls = AntiCheat.getInstance().profile.calls.get(string);
                    double pct = stringTotal / totalTime;
                    sender.sendMessage(Color.White + "Latency: " + stringTotal / calls + "ms");
                    sender.sendMessage(Color.White + "Calls: " + calls);
                    sender.sendMessage(Color.White + "STD: " + AntiCheat.getInstance().profile.stddev.get(string));
                    sender.sendMessage(Color.White + "PCT: " + MathUtils.round(pct, 8));
                    totalPCT += (pct);
                }
                sender.sendMessage(Color.Yellow + "Total PCT: " + Color.White + totalPCT);
                sender.sendMessage(Color.Yellow + "Total Time: " + Color.White + totalTime + "ms");
                sender.sendMessage(Color.Yellow + "Total Calls: " + Color.White + AntiCheat.getInstance().profile.totalCalls);
                AntiCheat.getInstance().profile = new ToggleableProfiler();
            } else {
                profileStart = System.currentTimeMillis();
            }
        } else if (args[1] != null && args[1].equalsIgnoreCase("specific")) {
            if (sender.getName().equalsIgnoreCase("funkemunky")
                    || sender.getName().equalsIgnoreCase("SmallWhiteAss")
                    || sender.getName().equalsIgnoreCase("XtasyCode")) {
                if (!AntiCheat.getInstance().specificProfile.enabled) {
                    AntiCheat.getInstance().specificProfile.enabled = true;
                    sender.sendMessage(AntiCheat.getInstance().getMessageFields().prefix + Color.White + Color.Italics + "You are now profiling a pre-set specific area of AntiCheat. This will end in 20 seconds");

                    new BukkitRunnable() {
                        public void run() {
                            AntiCheat.getInstance().specificProfile.enabled = false;
                            sender.sendMessage(Color.White + Color.Italics + "Profile complete! Results:");
                            for (String string : AntiCheat.getInstance().specificProfile.total.keySet()) {
                                sender.sendMessage(Color.Red + Color.Underline + string);
                                double stringTotal = TimeUnit.NANOSECONDS.toMillis(AntiCheat.getInstance().specificProfile.total.get(string));
                                int calls = AntiCheat.getInstance().specificProfile.calls.get(string);
                                double pct = stringTotal / TimeUnit.SECONDS.toMillis(20L);
                                sender.sendMessage(Color.White + "Latency: " + stringTotal / calls + "ms");
                                sender.sendMessage(Color.White + "Calls: " + calls);
                                sender.sendMessage(Color.White + "STD: " + AntiCheat.getInstance().specificProfile.stddev.get(string));
                                sender.sendMessage(Color.White + "PCT: " + MathUtils.round(pct, 8));
                            }
                            AntiCheat.getInstance().specificProfile = new ToggleableProfiler();
                        }
                    }.runTaskLater(AntiCheat.getInstance(), MathUtils.millisToTicks(TimeUnit.SECONDS.toMillis(20L)));
                } else {
                    sender.sendMessage(Color.Red + "There is a specific profile running right now. Please wait until it is complete.");
                }
            } else {
                sender.sendMessage(Color.Red + "This argument is a developer command and is not set for use by anyone else.");
            }
        } else {
            sender.sendMessage(AntiCheat.getInstance().getMessageFields().invalidArguments);
        }
    }
}
