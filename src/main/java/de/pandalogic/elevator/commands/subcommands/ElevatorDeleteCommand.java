package de.pandalogic.elevator.commands.subcommands;

import de.pandalogic.elevator.PLElevatorManager;
import de.pandalogic.elevator.commands.SubCommand;
import de.pandalogic.elevator.database.ElevatorDAO;
import de.pandalogic.elevator.utils.Message;
import org.bukkit.entity.Player;

public class ElevatorDeleteCommand extends SubCommand {

    @Override
    public String getDescription() {
        return "Deletes an elevator.";
    }

    @Override
    public String getSyntax() {
        return "/elevator delete <Id>";
    }

    @Override
    public String[] getNames() {
        return this.getArray("delete", "remove");
    }

    @Override
    public int arguments() {
        return 1;
    }

    @Override
    public void perform(Player player, String[] args) {
        try {
            int id = Integer.parseInt(args[1]);

            if (PLElevatorManager.findElevatorById(id) == null) {
                player.sendMessage(Message.ELEVATOR_NOT_EXISTS.replace(id).getMessage());
                return;
            }

            ElevatorDAO.removeElevator(id);
            player.sendMessage(Message.ELEVATOR_REMOVED.replace(id).getMessage());
        } catch (NumberFormatException e) {
            player.sendMessage(Message.INVALID_NUMBER.getMessage());
        }
    }

}
