package de.pandalogic.elevator.commands.subcommands;

import de.pandalogic.elevator.Elevator;
import de.pandalogic.elevator.PLElevatorManager;
import de.pandalogic.elevator.commands.SubCommand;
import de.pandalogic.elevator.database.ElevatorFloorDAO;
import de.pandalogic.elevator.utils.Message;
import org.bukkit.entity.Player;

public class ElevatorDeleteFloorCommand extends SubCommand {

    @Override
    public String getDescription() {
        return "Deletes a floor from an elevator.";
    }

    @Override
    public String getSyntax() {
        return "/elevator deleteFloor <ElevatorId> <FloorNumber>";
    }

    @Override
    public String[] getNames() {
        return this.getArray("deleteFloor", "removeFloor");
    }

    @Override
    public int arguments() {
        return 2;
    }

    @Override
    public void perform(Player player, String[] args) {
        try {
            int id = Integer.parseInt(args[1]);
            Elevator elevator = PLElevatorManager.findElevatorById(id);

            if (elevator == null) {
                player.sendMessage(Message.ELEVATOR_NOT_EXISTS.getMessage());
                return;
            }

            int floorNumber = Integer.parseInt(args[2]);

            if (elevator.getFloors().stream().noneMatch(f -> f.getNumber() == floorNumber)) {
                player.sendMessage(Message.ELEVATOR_FLOOR_NOT_EXISTS.replace(id).replace(floorNumber).getMessage());
                return;
            }

            ElevatorFloorDAO.removeFloor(id, floorNumber);
            player.sendMessage(Message.ELEVATOR_FLOOR_REMOVED.replace(floorNumber).replace(id).getMessage());
        } catch (NumberFormatException e) {
            player.sendMessage(Message.INTEGER_PARAMETER.getMessage());
        }
    }

}
