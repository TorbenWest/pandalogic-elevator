package de.pandalogic.elevator.commands.subcommands;

import de.pandalogic.elevator.Elevator;
import de.pandalogic.elevator.ElevatorPlugin;
import de.pandalogic.elevator.commands.SubCommand;
import de.pandalogic.elevator.utils.Message;
import org.bukkit.entity.Player;

public class ElevatorIdentifyCommand extends SubCommand {

    @Override
    public String getDescription() {
        return "Identifies the elevator where the player is in.";
    }

    @Override
    public String getSyntax() {
        return "/elevator identify";
    }

    @Override
    public String[] getNames() {
        return this.getArray("identify", "id");
    }

    @Override
    public int arguments() {
        return 0;
    }

    @Override
    public void perform(Player player, String[] args) {
        for (Elevator current : ElevatorPlugin.getInstance().getManager().getElevators()) {
            if (current.isPlayerInElevator(player)) {
                player.sendMessage(Message.ELEVATOR_ID.replace(current.getId()).getMessage());
                return;
            }
        }

        player.sendMessage(Message.PLAYER_NOT_IN_ELEVATOR.getMessage());
    }

}
