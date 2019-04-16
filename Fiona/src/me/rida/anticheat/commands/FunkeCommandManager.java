package me.rida.anticheat.commands;

import me.rida.anticheat.commands.anticheat.AntiCheatCommand;

import java.util.ArrayList;
import java.util.List;

public class FunkeCommandManager {
    public final List<FunkeCommand> commands;

    public FunkeCommandManager() {
        commands = new ArrayList<>();
        this.initialization();
    }

    private void initialization() {
        this.commands.add(new AntiCheatCommand());
    }

    public void removeAllCommands() {
        commands.clear();
    }
}

