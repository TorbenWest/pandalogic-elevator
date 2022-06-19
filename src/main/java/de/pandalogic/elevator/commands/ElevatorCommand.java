package de.pandalogic.elevator.commands;

import de.pandalogic.elevator.commands.subcommands.*;
import de.pandalogic.elevator.utils.Message;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ElevatorCommand implements PlayerCommandExecutor {

    private final List<SubCommand> subCommands;

    public ElevatorCommand() {
        this.subCommands = new ArrayList<>();
        this.subCommands.add(new ElevatorIdentifyCommand());
        this.subCommands.add(new ElevatorCreateCommand());
        this.subCommands.add(new ElevatorDeleteCommand());
        this.subCommands.add(new ElevatorCreateFloorCommand());
        this.subCommands.add(new ElevatorDeleteFloorCommand());
        this.subCommands.add(new ElevatorHelpCommand());
        this.subCommands.add(new ElevatorInfoCommand());
        this.subCommands.add(new ElevatorInventoryCommand());
        this.subCommands.add(new ElevatorSettingsCommand());
        this.subCommands.add(new ElevatorCopyFloorCommand());
    }

    @Override
    public void onCommand(Player player, Command command, String[] args) {
        if (args.length == 0) {
            player.sendMessage(Message.USAGE.replace(command.getUsage()).getMessage());
            return;
        }

        for (SubCommand current : this.subCommands) {
            if (Arrays.stream(current.getNames()).anyMatch(s -> s.equalsIgnoreCase(args[0]))) {
                if (current.getPermission() != null && !player.hasPermission(current.getPermission())) {
                    player.sendMessage(Message.NO_PERMISSION.getMessage());
                    return;
                }

                if (args.length != current.arguments() + 1) {
                    player.sendMessage(Message.USAGE.replace(current.getSyntax()).getMessage());
                    return;
                }

                current.perform(player, args);
                return;
            }
        }

        player.performCommand("elevator help");
    }

}
