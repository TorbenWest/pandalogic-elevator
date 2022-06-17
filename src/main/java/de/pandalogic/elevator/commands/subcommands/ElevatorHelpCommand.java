package de.pandalogic.elevator.commands.subcommands;

import de.pandalogic.elevator.ElevatorPlugin;
import de.pandalogic.elevator.commands.SubCommand;
import org.bukkit.entity.Player;

import static de.pandalogic.elevator.utils.MessageUtil.translate;

public class ElevatorHelpCommand extends SubCommand {

    @Override
    public String getDescription() {
        return "Lists all available sub commands.";
    }

    @Override
    public String getSyntax() {
        return "/elevator help";
    }

    @Override
    public String[] getNames() {
        return this.getArray("help");
    }

    @Override
    public int arguments() {
        return 0;
    }

    @Override
    public void perform(Player player, String[] args) {
        player.sendMessage(translate(ElevatorPlugin.PREFIX + "&8--- &6Elevator System by &4Pandalogic &8---"));
        player.sendMessage(translate("&7- &a/elevator create"));
        player.sendMessage(translate("&7- &a/elevator createFloor"));
        player.sendMessage(translate("&7- &a/elevator delete"));
        player.sendMessage(translate("&7- &a/elevator deleteFloor"));
        player.sendMessage(translate("&7- &a/elevator identify"));
        player.sendMessage(translate("&7- &a/elevator info"));
        player.sendMessage(translate("&7- &a/elevator inventory"));
        player.sendMessage(translate("&7- &a/elevator settings"));
        player.sendMessage(translate("&7- &a/elevator help"));
    }

}
