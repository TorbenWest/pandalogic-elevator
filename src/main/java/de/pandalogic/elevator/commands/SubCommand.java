package de.pandalogic.elevator.commands;

import de.pandalogic.elevator.utils.Message;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class SubCommand {

    protected final List<SubCommand> commands;

    protected SubCommand() {
        this.commands = new ArrayList<>();
        this.initSubSubCommands();
    }

    public void initSubSubCommands() {
    }

    public abstract String getDescription();

    public abstract String getSyntax();

    public String getPermission() {
        return null;
    }

    public abstract String[] getNames();

    public abstract int arguments();

    public void perform(Player player, String[] args) {
        for (SubCommand current : this.commands) {
            if (Arrays.stream(current.getNames()).anyMatch(s -> s.equalsIgnoreCase(args[1]))) {
                if (!player.hasPermission(current.getPermission())) {
                    player.sendMessage(Message.NO_PERMISSION.getMessage());
                    return;
                }

                int length = current.arguments() + 2;

                switch (this.getCompareType()) {
                    case UNEQUALS:
                        if (args.length != length) {
                            player.sendMessage(Message.USAGE.replace(current.getSyntax()).getMessage());
                            return;
                        }
                        break;
                    case EQUALS:
                        if (args.length == length) {
                            player.sendMessage(Message.USAGE.replace(current.getSyntax()).getMessage());
                            return;
                        }
                        break;
                    case GREATER:
                        if (args.length > length) {
                            player.sendMessage(Message.USAGE.replace(current.getSyntax()).getMessage());
                            return;
                        }
                        break;
                    case LESSER:
                        if (args.length < length) {
                            player.sendMessage(Message.USAGE.replace(current.getSyntax()).getMessage());
                            return;
                        }
                        break;
                }

                current.perform(player, args);
                return;
            }
        }

        player.sendMessage(Message.NO_COMMAND.getMessage());
    }

    protected final String[] getArray(String... name) {
        return name;
    }

    protected CompareType getCompareType() {
        return CompareType.UNEQUALS;
    }

    protected enum CompareType {
        GREATER,
        LESSER,
        EQUALS,
        UNEQUALS
    }

}
